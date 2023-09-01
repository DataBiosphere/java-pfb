package bio.terra.pfb.models.commands;

import bio.terra.pfb.Library;
import java.io.IOException;

public class ShowNodes implements PfbLibraryCommandInterface {
  @Override
  public String command(Library library, String filePath) throws IOException {
    return library.showNodes(filePath);
  }
}
