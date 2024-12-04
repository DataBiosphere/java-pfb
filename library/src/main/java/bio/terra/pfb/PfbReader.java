package bio.terra.pfb;

import bio.terra.pfb.exceptions.InvalidPfbException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileStream;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.specific.SpecificDatumReader;

public class PfbReader {

  // regex for decoding enums. See convertEnum().
  private static final Pattern ENUM_PATTERN = Pattern.compile("_([A-Fa-f0-9]{2,3})_");

  public static String showSchema(String fileLocation) throws IOException {
    // TODO AJ-1288: the use of convertEnum here is incorrect. It performs decoding on the entire
    //     string output of the schema. Instead, it should only perform decoding on the individual
    //     values of ENUM fields within schemas.
    return convertEnum(
        getPfbSchema(fileLocation).stream().map(Schema::toString).toList().toString());
  }

  public static String showNodes(String fileLocation) throws IOException {
    Metadata metadata = getPfbMetadata(fileLocation);
    return metadata.getNodes().stream()
        .map(Node::getName)
        .collect(Collectors.joining("\n", "", "\n"));
  }

  public static String showMetadata(String fileLocation) throws IOException {
    Metadata metadata = getPfbMetadata(fileLocation);
    return metadata.toString();
  }

  public static List<String> show(String fileLocation) throws IOException {
    List<String> data = new ArrayList<>();

    try (DataFileStream<GenericRecord> records = PfbReader.getGenericRecordsStream(fileLocation)) {
      while (records.hasNext()) {
        // TODO AJ-1288: the use of convertEnum here is incorrect. It performs decoding on the
        //     entire string output of a record. Instead, it should only perform decoding on the
        //     individual values of ENUM fields within that record.
        data.add(convertEnum(records.next().toString()));
      }
      return data;
    } catch (IOException e) {
      throw new InvalidPfbException("Error reading PFB Value object", e);
    }
  }

  /** DataFileStream implements Closeable and must be closed by the client code. */
  public static DataFileStream<GenericRecord> getGenericRecordsStream(String fileLocation)
      throws IOException {
    GenericDatumReader<GenericRecord> datumReader = new GenericDatumReader<>();
    URL url = isValidUrl(fileLocation);
    try {
      InputStream in =
          url != null ? readFromSignedUrl(url.toString()) : readFromLocalFile(fileLocation);
      var reader = new DataFileStream<>(in, datumReader);
      // advance past metadata
      reader.next();
      return reader;
    } catch (Exception e) {
      throw new InvalidPfbException("Error reading PFB Value object", e);
    }
  }

  public static Metadata getPfbMetadata(String fileLocation) throws IOException {
    DatumReader<Entity> datumReader = new SpecificDatumReader<>(Entity.class);
    URL url = isValidUrl(fileLocation);
    Entity data = null;
    try (InputStream in =
            url != null ? readFromSignedUrl(url.toString()) : readFromLocalFile(fileLocation);
        DataFileStream<Entity> reader = new DataFileStream<>(in, datumReader)) {
      // A PFB Avro file consists of a list of "Entity" objects (Defined in sample.advl)
      // One of these entities must be a "Metadata" object (Also defined in sample.advl)
      // The rest of the entities are the data entries
      // Assuming the first object is the metadata object
      data = reader.next(data);
      Metadata result = (Metadata) data.getObject();
      if (result != null) {
        return result;
      }
    }
    throw new InvalidPfbException("Error reading PFB Metadata object");
  }

  // note that this does not decode enum values via convertEnum. WDS does not need decoding;
  // if other clients do need it we should add it in here.
  public static List<Schema> getPfbSchema(String fileLocation) throws IOException {
    return readPfbSchema(fileLocation).getField("object").schema().getTypes().stream()
        .filter(t -> !t.getName().equals("Metadata"))
        .toList();
  }

  static Schema readPfbSchema(String fileLocation) throws IOException {
    DatumReader<Entity> datumReader = new SpecificDatumReader<>(Entity.class);
    URL url = isValidUrl(fileLocation);
    try (InputStream in =
            url != null ? readFromSignedUrl(url.toString()) : readFromLocalFile(fileLocation);
        DataFileStream<Entity> reader = new DataFileStream<>(in, datumReader)) {
      return reader.getSchema();
    }
  }

  // Helper methods
  static URL isValidUrl(String fileLocation) {
    try {
      return new URL(fileLocation);
    } catch (IOException e) {
      return null;
    }
  }

  static InputStream readFromLocalFile(String filePath) throws IOException {
    return new FileInputStream(filePath);
  }

  static InputStream readFromSignedUrl(String signedUrl) throws IOException {
    URL urlObject = new URL(signedUrl);
    URLConnection urlConnection = urlObject.openConnection();
    return urlConnection.getInputStream();
  }

  /*

  */

  /**
   * Need to instead encode/decode enum typed variables From docs (<a
   * href="https://github.com/uc-cdis/pypfb/blob/master/docs/index.md#enum">...</a>): "Because Avro
   * can't store anything except alphanumeric and _ symbols (<a
   * href="https://avro.apache.org/docs/1.9.1/spec.html#Enums">...</a>), all enums are encoded in
   * such a way, that all other symbols is being encoded with codepoint wrapped in single
   * underscores. For example bpm > 60 will be encoded in: bpm_32__62__32_60, so space is encoded as
   * _32_ and greater sign > into _62_. Same for Unicode characters: ä - _228_, ü - _252_. The Avro
   * schema also doesn't allow for the first character to be a number. So we encode the first
   * character in the way if the character happens to be a number."
   *
   * <p>IMPORTANT: the above quote from the pypfb repo is incorrect - "bpm > 60" will actually be
   * encoded as "bpm_20__3e__20_60", because encoding uses hex instead of decimal codepoints.
   *
   * <p>This method has slightly different behavior than pypfb: this method does not decode any
   * characters below codepoint 32, as those are control characters and deemed unsafe.
   *
   * @param enumValue the input that needs decoding
   * @return the decoded value
   */
  public static String convertEnum(String enumValue) {
    // get matcher for this input
    Matcher matcher = ENUM_PATTERN.matcher(enumValue);

    return matcher.replaceAll(
        capture -> {
          int codepoint = Integer.parseInt(capture.group(1), 16);
          // don't decode control characters
          if (codepoint > 31) {
            return new String(Character.toChars(codepoint));
          } else {
            // this is a control character; don't replace anything
            return capture.group(0);
          }
        });
  }
}
