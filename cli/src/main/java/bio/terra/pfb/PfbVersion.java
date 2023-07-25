package bio.terra.pfb;

import picocli.CommandLine;

public class PfbVersion implements CommandLine.IVersionProvider {

  // CLI_VERSION must be manually upgraded and resulting jar deployed to artifactory
  // See cli/README for publishing instructions
  private static final String CLI_VERSION = "0.1.0";

  @Override
  public String[] getVersion() {
    return new String[] {"${COMMAND-FULL-NAME} " + CLI_VERSION};
  }
}
