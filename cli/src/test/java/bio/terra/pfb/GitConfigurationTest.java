package bio.terra.pfb;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;

import org.junit.jupiter.api.Test;

class GitConfigurationTest {
  @Test
  void getBuildVersion() {
    GitConfiguration gitConfiguration = new GitConfiguration();
    String version = gitConfiguration.getCliVersion();
    assertThat(version, matchesPattern("\\d+\\.\\d+\\.\\d"));
  }
}
