package bio.terra.pfb.models.commands;

import bio.terra.pfb.Library;
import java.io.IOException;

public class ShowMetadata implements PfbLibraryCommandInterface {
  @Override
  public String command(String filePath) throws IOException {
    return Library.showMetadata(filePath);
  }
}
