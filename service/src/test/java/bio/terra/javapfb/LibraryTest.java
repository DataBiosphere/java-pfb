package bio.terra.javapfb;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({"test", "human-readable-logging"})
public abstract class LibraryTest {
  @Test
  public void testHelloWorld() {
    System.out.println("Hello World");
  }
}
