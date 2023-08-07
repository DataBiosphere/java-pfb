package bio.terra.pfb;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
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
    return pfbSchemaShow.stream().map(s -> s.toString()).toList().toString();
  }

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
}
