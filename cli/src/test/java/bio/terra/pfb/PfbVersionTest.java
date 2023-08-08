package bio.terra.pfb;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;

import org.junit.jupiter.api.Test;

class PfbVersionTest {

  @Test
  void getVersion() {
    PfbVersion pfbVersion = new PfbVersion();
    String[] version = pfbVersion.getVersion();
    assertThat(version[0], matchesPattern("\\$\\{COMMAND-FULL-NAME\\} \\d+\\.\\d+\\.\\d"));
  }
}
