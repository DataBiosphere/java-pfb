package bio.terra.pfb;

import java.util.stream.Collectors;

public class Library {
  private PfbReader pfbReader;

  public Library(PfbReader pfbReader) {
    this.pfbReader = pfbReader;
  }

  public String showSchema(String fileLocation) {
    return pfbReader.showSchema(fileLocation);
  }

  public String showNodes(String fileLocation) {
    return pfbReader.showNodes(fileLocation);
  }

  public String showMetadata(String fileLocation) {
    return pfbReader.showMetadata(fileLocation);
  }

  public String showTableRows(String fileLocation) {
    return pfbReader.show(fileLocation).stream().collect(Collectors.joining("\n"));
  }
}
