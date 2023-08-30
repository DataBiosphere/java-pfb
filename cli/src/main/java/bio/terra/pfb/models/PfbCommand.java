package bio.terra.pfb.models;

public enum PfbCommand {
  SHOW("show");

  private final String displayName;

  PfbCommand(String displayName) {
    this.displayName = displayName;
  }

  @Override
  public String toString() {
    return this.displayName;
  }
}
