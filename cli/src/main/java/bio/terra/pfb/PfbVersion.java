package bio.terra.pfb;

import picocli.CommandLine;

public class PfbVersion implements CommandLine.IVersionProvider {

  private static final String CLI_VERSION = "0.1.0";

  @Override
  public String[] getVersion() {
    // TODO: Figure out how to get the version from the settings.gradle
    return new String[] {"${COMMAND-FULL-NAME} " + CLI_VERSION};
  }
}
