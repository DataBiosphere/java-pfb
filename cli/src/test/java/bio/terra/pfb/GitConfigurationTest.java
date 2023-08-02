package bio.terra.pfb;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.nullValue;

import org.junit.jupiter.api.Test;

class GitConfigurationTest {

  @Test
  void getBuildVersion() {
    GitConfiguration gitConfiguration = new GitConfiguration();
    String version = gitConfiguration.getCliVersion();
    assertThat(version, matchesPattern("\\d+\\.\\d+\\.\\d"));
  }

  @Test
  void testFileNotFound() {
    GitConfiguration gitConfiguration = new GitConfiguration();
    String emptyString = gitConfiguration.readGitPropertiesValue("invalid.property.name");
    assertThat(emptyString, is(nullValue()));
  }
}
