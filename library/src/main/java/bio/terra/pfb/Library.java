package bio.terra.pfb;

import java.util.stream.Collectors;

public class Library {
  private Library() {
    // intentionally empty
  }

  public static String showSchema(String fileLocation) {
    PfbReader pfbReader = new PfbReader();
    return pfbReader.showSchema(fileLocation);
  }

  public static String showNodes(String fileLocation) {
    PfbReader pfbReader = new PfbReader();
    return pfbReader.showNodes(fileLocation);
  }

  public static String showMetadata(String fileLocation) {
    PfbReader pfbReader = new PfbReader();
    return pfbReader.showMetadata(fileLocation);
  }

  public static String showTableRows(String fileLocation) {
    PfbReader pfbReader = new PfbReader();
    return pfbReader.show(fileLocation).stream().collect(Collectors.joining("\n"));
  }
}
