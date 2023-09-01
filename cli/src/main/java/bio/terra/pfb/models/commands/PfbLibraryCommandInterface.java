package bio.terra.pfb.models.commands;

import java.io.IOException;

public interface PfbLibraryCommandInterface {
  default String command(String filePath) throws IOException {
    throw new UnsupportedOperationException(ERROR_MESSAGE);
  }

  default String commandWithLimit(String filePath, int limit) throws IOException {
    throw new UnsupportedOperationException(ERROR_MESSAGE);
  }

  String ERROR_MESSAGE = "Invalid command";
}
