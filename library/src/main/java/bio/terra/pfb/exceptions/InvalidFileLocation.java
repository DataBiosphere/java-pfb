package bio.terra.pfb.exceptions;

import java.io.IOException;

public class InvalidFileLocation extends IOException {
  public InvalidFileLocation(String message) {
    super(message);
  }
}
