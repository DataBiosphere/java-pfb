package bio.terra.pfb;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileStream;
import org.apache.avro.io.DatumReader;
import org.apache.avro.specific.SpecificDatumReader;

public class PfbReader {

  public String showSchema(String fileLocation) throws IOException {
    Schema schema = getSchema(fileLocation);
    List<Schema> pfbSchemaShow =
        schema.getField("object").schema().getTypes().stream()
            .filter(t -> !t.getName().equals("Metadata"))
            .toList();
    String show = pfbSchemaShow.stream().map(s -> s.toString()).toList().toString();
    return convertEnum(show);
  }

  // List particular enum symbols
  // schema.getField("object").schema().getTypes().get(3).getFields().get(1).schema().getTypes().stream().filter(s -> s.getType().equals(Schema.Type.ENUM)).findFirst().get().getEnumSymbols()

  public Schema getSchema(String fileLocation) throws IOException {
    boolean isUrl = isValidUrl(fileLocation);
    if (isUrl) {
      return readUrlPFBSchema(fileLocation);
    }
    return readFilePathPFBSchema(fileLocation);
  }

  Schema readFilePathPFBSchema(String fileLocation) throws IOException {
    DatumReader<Entity> datumReader = new SpecificDatumReader<>(Entity.class);
    DataFileReader<Entity> dataFileReader =
        new DataFileReader<>(new File(fileLocation), datumReader);
    return dataFileReader.getSchema();
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

  /* TODO - Remove this method.
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
    HashMap<String, String> map = new HashMap<>();
    map.put("_20_", " ");
    map.put("_21_", "!");
    map.put("_22_", "\"");
    map.put("_23_", "#");
    map.put("_24_", "$");
    map.put("_25_", "%");
    map.put("_26_", "&");
    map.put("_27_", "'");
    map.put("_28_", "(");
    map.put("_29_", ")");
    map.put("_2a_", "*");
    map.put("_2b_", "+");
    map.put("_2c_", ",");
    map.put("_2d_", "-");
    map.put("_2f_", "/");
    map.put("_30_", "0");
    map.put("_31_", "1");
    map.put("_32_", "2");
    map.put("_33_", "3");
    map.put("_34_", "4");
    map.put("_35_", "5");
    map.put("_36_", "6");
    map.put("_37_", "7");
    map.put("_38_", "8");
    map.put("_39_", "9");
    map.put("_3a_", ":");
    map.put("_3b_", ";");
    map.put("_3c_", "<");
    map.put("_3d_", "=");
    map.put("_3e_", ">");
    map.put("_3f_", "?");
    map.put("_5b_", "[");
    map.put("_5c_", "\\");
    map.put("_5d_", "]");
    map.put("_5e_", "^");
    map.put("_5f_", "_");
    map.put("_60_", "`");
    map.put("_7b_", "{");
    map.put("_7c_", "|");
    map.put("_7d_", "}");
    map.put("_7e_", "~");

    for (String key : map.keySet()) {
      if (schema.contains(key)) {
        schema = schema.replace(key, map.get(key));
      }
    }
    return schema;
  }
}
