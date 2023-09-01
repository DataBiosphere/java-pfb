package bio.terra.pfb.models.commands;

import bio.terra.pfb.Library;
import java.io.IOException;

public class TableRows implements PfbLibraryCommandInterface {
  @Override
  public String command(Library library, String filePath) throws IOException {
    return library.showTableRows(filePath);
  }

  @Override
  public String commandWithLimit(Library library, String filePath, int limit) throws IOException {
    return library.showTableRows(filePath, limit);
  }
}
