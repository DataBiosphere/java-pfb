package bio.terra.pfb;

import bio.terra.pfb.exceptions.InvalidFileLocation;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class PfbReader {

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
