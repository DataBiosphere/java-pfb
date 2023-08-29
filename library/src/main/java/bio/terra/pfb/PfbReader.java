package bio.terra.pfb;

import bio.terra.pfb.exceptions.InvalidPfbException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileStream;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.specific.SpecificDatumReader;

public class PfbReader {
  private final String CHAR_LIST = " !\"_#$%&'()*+-/.0123456789:;,<=>?[\\]^`{|}~";
  private final Map<String, String> ENCODED_ENUM_TO_CHAR_MAP =
      CHAR_LIST
          .chars()
          .boxed()
          .collect(
              Collectors.toMap(c -> "_" + String.format("%02x", c) + "_", Character::toString));

  public String showSchema(String fileLocation) {
    try {
      Schema schema = getSchema(fileLocation);
      List<Schema> pfbSchemaShow =
          schema.getField("object").schema().getTypes().stream()
              .filter(t -> !t.getName().equals("Metadata"))
              .toList();
      String show = pfbSchemaShow.stream().map(Schema::toString).toList().toString();
      return convertEnum(show);
    } catch (IOException e) {
      return getErrorMessage(e);
    }
  }

  public Schema getSchema(String fileLocation) throws IOException {
    boolean isUrl = isValidUrl(fileLocation);
    if (isUrl) {
      return readUrlPFBSchema(fileLocation);
    }
    return readFilePathPFBSchema(fileLocation);
  }

  public String showNodes(String fileLocation) {
    try {
      Metadata metadata = getPFBMetadata(fileLocation);
      return metadata.getNodes().stream().map(Node::getName).collect(Collectors.joining("\n"))
          + "\n";
    } catch (IOException e) {
      return getErrorMessage(e);
    }
  }

  public String showMetadata(String fileLocation) {
    try {
      Metadata metadata = getPFBMetadata(fileLocation);
      return metadata.toString();
    } catch (Exception e) {
      return getErrorMessage(e);
    }
  }

  Schema readFilePathPFBSchema(String fileLocation) throws IOException {
    DatumReader<Entity> datumReader = new SpecificDatumReader<>(Entity.class);
    try (DataFileReader<Entity> dataFileReader =
        new DataFileReader<>(new File(fileLocation), datumReader)) {
      return dataFileReader.getSchema();
    }
  }

  // read generic avro data from file
  public List<String> show(String fileLocation) {
    File pfbData = new File(fileLocation);
    // Deserialize the above generated avro data file
    GenericDatumReader<GenericRecord> datumReader = new GenericDatumReader<>();
    try (DataFileReader<GenericRecord> dataFileReader =
        new DataFileReader<>(pfbData, datumReader)) {
      GenericRecord genericRecord = null;
      List<String> data = new ArrayList<>();
      // Skip Metadata Object, which should always appear first
      dataFileReader.next(genericRecord);
      while (dataFileReader.hasNext()) {
        genericRecord = dataFileReader.next(genericRecord);
        data.add(convertEnum(genericRecord.toString()));
      }
      return data;
    } catch (IOException e) {
      return List.of(getErrorMessage(e));
    }
  }

  public Metadata getPFBMetadata(String fileLocation) throws IOException {
    File pfbData = new File(fileLocation);
    // Deserialize the above generated avro data file
    DatumReader<Entity> datumReader = new SpecificDatumReader<>(Entity.class);
    Entity data = null;
    Metadata result = null;
    try (DataFileReader<Entity> dataFileReader = new DataFileReader<>(pfbData, datumReader)) {
      // A PFB Avro file consists of a list of "Entity" objects (Defined in sample.advl)
      // One of these entities must be a "Metadata" object (Also defined in sample.advl)
      // The rest of the entities are the data entries
      // Assuming the first object is the metadata object
      data = dataFileReader.next(data);
      result = (Metadata) data.getObject();
      if (result != null) {
        return result;
      }
    }
    throw new InvalidPfbException("Error reading PFB Metadata object");
  }

  Schema readUrlPFBSchema(String signedUrl) throws IOException {
    Schema schema;
    DatumReader<Entity> datumReader = new SpecificDatumReader<>(Entity.class);
    try (InputStream in = readFromSignedUrl(signedUrl);
        DataFileStream<Entity> reader = new DataFileStream<>(in, datumReader)) {
      schema = reader.getSchema();
    } catch (IOException ex) {
      throw new IOException("Error reading PFB file from signed URL", ex);
    }
    return schema;
  }

  // Helper methods

  private String getErrorMessage(Exception e) {
    return "Error: " + e.getMessage();
  }

  boolean isValidUrl(String fileLocation) {
    try {
      new URL(fileLocation);
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  InputStream readFromSignedUrl(String signedUrl) throws IOException {
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
  private String convertEnum(String schema) {
    for (Map.Entry<String, String> entry : ENCODED_ENUM_TO_CHAR_MAP.entrySet()) {
      schema = schema.replace(entry.getKey(), entry.getValue());
    }
    return schema;
  }
}
