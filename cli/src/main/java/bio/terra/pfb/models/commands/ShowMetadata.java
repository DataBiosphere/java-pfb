package bio.terra.pfb.models.commands;

import bio.terra.pfb.Library;
import java.io.IOException;

public class ShowMetadata implements PfbLibraryCommandInterface {
  @Override
  public String command(Library library, String filePath) throws IOException {
    return library.showMetadata(filePath);
  }

  @Override
  public String infoMessage(String filePath) {
    return "show metadata for file path: " + filePath;
  }
}
