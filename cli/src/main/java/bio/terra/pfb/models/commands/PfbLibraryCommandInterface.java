package bio.terra.pfb.models.commands;

import bio.terra.pfb.Library;
import java.io.IOException;

public interface PfbLibraryCommandInterface {
  default String command(Library library, String filePath) throws IOException {
    throw new UnsupportedOperationException(ERROR_MESSAGE);
  }

  default String commandWithLimit(Library library, String filePath, int limit) throws IOException {
    throw new UnsupportedOperationException(ERROR_MESSAGE);
  }

  String ERROR_MESSAGE = "Invalid command";
}
