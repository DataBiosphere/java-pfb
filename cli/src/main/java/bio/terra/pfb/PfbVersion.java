package bio.terra.pfb;

import picocli.CommandLine;

public class PfbVersion implements CommandLine.IVersionProvider {

  @Override
  public String[] getVersion() {
    // TODO: Figure out how to get the version from the settings.gradle
    return new String[] {"${COMMAND-FULL-NAME} 0.1.0"};
  }
}
