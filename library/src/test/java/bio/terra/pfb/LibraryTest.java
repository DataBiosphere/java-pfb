package bio.terra.pfb;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

class LibraryTest {

  @Test
  void testStubMethod() {
    assertThat(Library.getNumber5(), equalTo(5));
  }
}
