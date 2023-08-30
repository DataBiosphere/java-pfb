package bio.terra.pfb;

import java.io.IOException;
import java.util.stream.Collectors;

public record Library(PfbReader pfbReader) {

  public String showSchema(String fileLocation) throws IOException {
    return pfbReader.showSchema(fileLocation);
  }

  public String showNodes(String fileLocation) throws IOException {
    return pfbReader.showNodes(fileLocation);
  }

  public String showMetadata(String fileLocation) throws IOException {
    return pfbReader.showMetadata(fileLocation);
  }

  public String showTableRows(String fileLocation) throws IOException {
    return String.join("\n", pfbReader.show(fileLocation));
  }

  public String showTableRows(String fileLocation, int limit) throws IOException {
    return pfbReader.show(fileLocation).stream().limit(limit).collect(Collectors.joining("\n"));
  }
}
