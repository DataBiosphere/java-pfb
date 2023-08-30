package bio.terra.pfb.models;

public enum PfbCommandOption {
  SCHEMA("schema"),
  METADATA("metadata"),
  NODES("nodes"),
  TABLE_ROWS("tableRows");

  private final String displayName;

  PfbCommandOption(String displayName) {
    this.displayName = displayName;
  }

  @Override
  public String toString() {
    return this.displayName;
  }
}
