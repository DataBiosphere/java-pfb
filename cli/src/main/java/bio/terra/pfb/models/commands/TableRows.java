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

  @Override
  public String infoMessage(String filePath) {
    return String.format("show table rows for file path: %s", filePath);
  }

  @Override
  public String infoMessage(String filePath, int limit) {
    return String.format("show table rows for file path %s and limit %d", filePath, limit);
  }
}
