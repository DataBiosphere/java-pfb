package bio.terra.pfb;

import picocli.CommandLine;

public class PfbVersion implements CommandLine.IVersionProvider {
  @Override
  public String[] getVersion() {
    return new String[] {"${COMMAND-FULL-NAME} ${JAVAPFB_VERSION_BUILD}"};
  }
}
