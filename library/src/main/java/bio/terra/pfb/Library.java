package bio.terra.pfb;

import java.io.IOException;

public class Library {

  public static String showSchema(String fileLocation) throws IOException {
    return PfbReader.showSchema(fileLocation);
  }

  public static String showNodes(String fileLocation) throws IOException {
    return PfbReader.showNodes(fileLocation);
  }

  public static String showMetadata(String fileLocation) throws IOException {
    return PfbReader.showMetadata(fileLocation);
  }

  public static String showTableRows(String fileLocation) throws IOException {
    return PfbReader.show(fileLocation, -1);
  }

  public static String showTableRows(String fileLocation, int limit) throws IOException {
    return PfbReader.show(fileLocation, limit);
  }
}
