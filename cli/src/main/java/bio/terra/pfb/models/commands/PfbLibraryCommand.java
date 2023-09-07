package bio.terra.pfb.models.commands;

import java.io.IOException;
import picocli.CommandLine.PicocliException;

public interface PfbLibraryCommand {
  String ERROR_MESSAGE = "Invalid command";

  default String command(String filePath) throws IOException {
    throw new UnsupportedOperationException(ERROR_MESSAGE);
  }

  default String commandWithLimit(String filePath, int limit) throws IOException {
    throw new UnsupportedOperationException(ERROR_MESSAGE);
  }

  default void callCommand(String filePath) {
    callCommand(filePath, -1);
  }

  default void callCommand(String filePath, int limit) {
    if (limit >= 0) {
      String result;
      try {
        result = commandWithLimit(filePath, limit);
        System.out.println(result);
      } catch (IOException e) {
        throw new PicocliException(e.getMessage(), e);
      }
    } else {
      String result;
      try {
        result = command(filePath);
        System.out.println(result);
      } catch (IOException e) {
        throw new PicocliException(e.getMessage(), e);
      }
    }
  }
}
