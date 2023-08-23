package bio.terra.pfb;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class JavaPfbCommandTest {
  JavaPfbCommand javaPfbCommand;
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;
  private final PrintStream originalErr = System.err;

  @BeforeEach
  void setup() {
    javaPfbCommand = new JavaPfbCommand();
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
  }

  @AfterEach
  public void restoreStreams() {
    System.setOut(originalOut);
    System.setErr(originalErr);
  }

  @Test
  void run() {
    javaPfbCommand.run();
    assertThat(outContent.toString(), containsString("PFB RUN"));
  }

  @ParameterizedTest
  @ValueSource(strings = {"nodes", "metadata", "schema", ""})
  void testShowCommand(String option) {
    String[] args = new String[2];
    args[0] = "show";
    args[1] = option;
    javaPfbCommand.executeCommand(args);
    assertThat(outContent.toString(), containsStringIgnoringCase(option));
  }

  @Test
  void filePath() {
    String[] args = new String[2];
    args[0] = "show";
    args[1] = "-i /path/to/file";
    javaPfbCommand.executeCommand(args);
    assertThat(outContent.toString(), containsString("path/to/file"));
  }

  @Test
  void testVersionCommand() {
    String[] args = new String[1];
    args[0] = "--version";
    javaPfbCommand.executeCommand(args);
    assertThat(outContent.toString(), matchesPattern("pfb \\d+\\.\\d+\\.\\d\n"));
  }

  @Test
  void testCorrectVersion() {
    GitConfiguration gitConfiguration = new GitConfiguration();
    String exepectedVersion = gitConfiguration.getCliVersion();
    String[] args = new String[1];
    args[0] = "--version";
    javaPfbCommand.executeCommand(args);
    assertThat(outContent.toString(), equalTo("pfb " + exepectedVersion + "\n"));
  }

  @Test
  void testHelpCommand() {
    String[] args = new String[1];
    args[0] = "--help";
    javaPfbCommand.executeCommand(args);
    assertThat(outContent.toString(), containsString("Usage: pfb"));
  }
}
