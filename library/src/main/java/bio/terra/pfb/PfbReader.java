package bio.terra.pfb;

import java.io.*;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;

public class PfbReader {

  // TODO -- Need to figure out how to reference "Entity" from generated models
  //  public Entity deSerializeAvroHttpRequestJSON(byte[] data) throws IOException {
  //    DatumReader<Entity> reader = new SpecificDatumReader<>(Entity.class);
  //    Decoder decoder = null;
  //    try {
  //      decoder = DecoderFactory.get().jsonDecoder(Entity.getClassSchema(), new String(data));
  //      return reader.read(null, decoder);
  //    } catch (IOException e) {
  //      throw new IOException("Deserialization Error", e);
  //    }
  //  }

  public Schema readGenericSchema(String fileLocation) throws IOException {
    DatumReader<GenericRecord> datumReader = new GenericDatumReader<>();
    DataFileReader<GenericRecord> dataFileReader =
        new DataFileReader<>(new File(fileLocation), datumReader);
    return dataFileReader.getSchema();
  }

  // TODO - Can probably remove the code below as the there is data file reader from Apache Avro
  //  public String readPFBFile(String fileLocation) throws IOException {
  //    InputStream inputStream;
  //    try {
  //      if (isValidUrl(fileLocation)) {
  //        inputStream = readFromSignedUrl(fileLocation);
  //      } else {
  //        inputStream = readFileFromClassPath(fileLocation);
  //      }
  //    } catch (Exception e) {
  //      throw new InvalidFileLocation(fileLocation);
  //    }
  //    return readFromInputStream(inputStream);
  //  }
  //
  //  boolean isValidUrl(String fileLocation) {
  //    try {
  //      new URL(fileLocation);
  //      return true;
  //    } catch (IOException e) {
  //      return false;
  //    }
  //  }
  //
  //  InputStream readFromSignedUrl(String signedUrl) throws IOException {
  //    URL urlObject = new URL(signedUrl);
  //    URLConnection urlConnection = urlObject.openConnection();
  //    return urlConnection.getInputStream();
  //  }
  //
  //  InputStream readFileFromClassPath(String filePath) {
  //    Class clazz = PfbReader.class;
  //    return clazz.getResourceAsStream(filePath);
  //  }
  //
  //  String readFromInputStream(InputStream inputStream) throws IOException {
  //    StringBuilder resultStringBuilder = new StringBuilder();
  //    try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
  //      String line;
  //      // TODO - we probably want to do something per line
  //      while ((line = br.readLine()) != null) {
  //        resultStringBuilder.append(line).append("\n");
  //      }
  //    } catch (IOException e) {
  //      throw new IOException("Error reading from input stream", e);
  //    }
  //    return resultStringBuilder.toString();
  //  }
}
