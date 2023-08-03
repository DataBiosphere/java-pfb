package bio.terra.pfb;

import bio.terra.pfb.exceptions.InvalidFileLocation;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;

public class PfbReader {

  //  public Metadata deSerializeAvroHttpRequestJSON(byte[] data) throws IOException {
  //    DatumReader<Metadata> reader = new SpecificDatumReader<>(Metadata.class);
  //    Decoder decoder = null;
  //    try {
  //      decoder = DecoderFactory.get().jsonDecoder(Metadata.getClassSchema(), new String(data));
  //      return reader.read(null, decoder);
  //    } catch (IOException e) {
  //      throw new IOException("Deserialization Error", e);
  //    }
  //  }

  public Schema readSchema(String fileLocation) throws IOException {
    DatumReader<GenericRecord> datumReader = new GenericDatumReader<>();
    DataFileReader<GenericRecord> dataFileReader =
        new DataFileReader<>(new File(fileLocation), datumReader);
    return dataFileReader.getSchema();
  }

  public String readPFBFile(String fileLocation) throws IOException {
    InputStream inputStream;
    try {
      if (isValidUrl(fileLocation)) {
        inputStream = readFromSignedUrl(fileLocation);
      } else {
        inputStream = readFileFromClassPath(fileLocation);
      }
    } catch (Exception e) {
      throw new InvalidFileLocation(fileLocation);
    }
    return readFromInputStream(inputStream);
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

  InputStream readFileFromClassPath(String filePath) {
    Class clazz = PfbReader.class;
    return clazz.getResourceAsStream(filePath);
  }

  String readFromInputStream(InputStream inputStream) throws IOException {
    StringBuilder resultStringBuilder = new StringBuilder();
    try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
      String line;
      // TODO - we probably want to do something per line
      while ((line = br.readLine()) != null) {
        resultStringBuilder.append(line).append("\n");
      }
    } catch (IOException e) {
      throw new IOException("Error reading from input stream", e);
    }
    return resultStringBuilder.toString();
  }
}
