package bio.terra.pfb.exceptions;

public class InvalidPfbException extends RuntimeException {
  public InvalidPfbException(String message) {
    super(message);
  }

  public InvalidPfbException(String message, Throwable cause) {
    super(message, cause);
  }
}
