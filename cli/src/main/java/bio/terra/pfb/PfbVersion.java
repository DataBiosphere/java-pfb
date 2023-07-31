package bio.terra.pfb;

import picocli.CommandLine;

public class PfbVersion implements CommandLine.IVersionProvider {

  @Override
  public String[] getVersion() {
    GitConfiguration gitConfiguration = new GitConfiguration();
    return new String[] {
      String.format("${COMMAND-FULL-NAME} %s", gitConfiguration.getCliVersion())
    };
  }
}
