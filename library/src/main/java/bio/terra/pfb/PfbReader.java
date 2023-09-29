package bio.terra.pfb;

import bio.terra.pfb.exceptions.InvalidPfbException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileStream;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.specific.SpecificDatumReader;

public class PfbReader {
  private static final String SYMBOL_LIST = " !\"_#$%&'()*+-/.0123456789:;,<=>?[\\]^`{|}~";
  private static final Map<String, String> ENCODED_ENUM_TO_SYMBOL_MAP =
      SYMBOL_LIST
          .chars()
          .boxed()
          .collect(
              Collectors.toMap(c -> "_" + String.format("%02x", c) + "_", Character::toString));

  private DataFileStream<GenericRecord> stream;

  public static String showSchema(String fileLocation) throws IOException {
    return convertEnum(
        readPfbSchema(fileLocation).getField("object").schema().getTypes().stream()
            .filter(t -> !t.getName().equals("Metadata"))
            .map(Schema::toString)
            .toList()
            .toString());
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
    try {
      DataFileStream<GenericRecord> records = PfbReader.getGenericRecords(fileLocation);

      while (records.hasNext()) {
        data.add(convertEnum(records.next().toString()) + "\n");
      }
      return data;
    } catch (IOException e) {
      throw new InvalidPfbException("Error reading PFB Value object");
    }
  }

  public static DataFileStream<GenericRecord> getGenericRecords(String fileLocation)
      throws IOException {
    GenericDatumReader<GenericRecord> datumReader = new GenericDatumReader<>();
    URL url = isValidUrl(fileLocation);

    InputStream in =
        url != null ? readFromSignedUrl(url.toString()) : readFromLocalFile(fileLocation);
    var reader = new DataFileStream<>(in, datumReader);
    // advance past metadata
    reader.next();
    return reader;
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

  /* This method will be removed in AJ-1288
       Need to instead encode/decode enum typed variables
     From docs (https://github.com/uc-cdis/pypfb/blob/master/docs/index.md#enum):
    "Because Avro can't store anything except alphanumeric and _ symbols
    (https://avro.apache.org/docs/1.9.1/spec.html#Enums), all enums are encoded in
    such a way, that all other symbols is being encoded with codepoint wrapped in
    single underscores. For example bpm > 60 will be encoded in: bpm_32__62__32_60,
    so space   is encoded as _32_ and greater sign > into _62_. Same for Unicode
    characters: ä - _228_, ü - _252_. The Avro schema also doesn't allow for the
    first character to be a number. So we encode the first character in the way if
    the character happens to be a number."
  */
  private static String convertEnum(String schema) {
    for (Map.Entry<String, String> entry : ENCODED_ENUM_TO_SYMBOL_MAP.entrySet()) {
      schema = schema.replace(entry.getKey(), entry.getValue());
    }
    return schema;
  }
}
