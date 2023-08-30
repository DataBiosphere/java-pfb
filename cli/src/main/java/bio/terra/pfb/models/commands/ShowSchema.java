package bio.terra.pfb.models.commands;

import bio.terra.pfb.Library;
import java.io.IOException;

public class ShowSchema implements PfbLibraryCommandInterface {
  @Override
  public String command(Library library, String filePath) throws IOException {
    return library.showSchema(filePath);
  }

  @Override
  public String infoMessage(String filePath) {
    return "show schema for file path: " + filePath;
  }
}
