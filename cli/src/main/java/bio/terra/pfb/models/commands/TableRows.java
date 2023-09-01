package bio.terra.pfb.models.commands;

import bio.terra.pfb.Library;
import java.io.IOException;

public class TableRows implements PfbLibraryCommandInterface {
  @Override
  public String command(String filePath) throws IOException {
    return Library.showTableRows(filePath);
  }

  @Override
  public String commandWithLimit(String filePath, int limit) throws IOException {
    return Library.showTableRows(filePath, limit);
  }
}
