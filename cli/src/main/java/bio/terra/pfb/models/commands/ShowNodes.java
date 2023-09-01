package bio.terra.pfb.models.commands;

import bio.terra.pfb.Library;
import java.io.IOException;

public class ShowNodes implements PfbLibraryCommandInterface {
  @Override
  public String command(String filePath) throws IOException {
    return Library.showNodes(filePath);
  }
}
