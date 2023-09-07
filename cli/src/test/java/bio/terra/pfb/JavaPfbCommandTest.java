package bio.terra.pfb;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.matchesPattern;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
  void invalidFilePath() {
    testCommandStringMatches(
        List.of("show", "-i invalid/file/path"), errContent, "(No such file or directory)");
  }

  @Test
  void testVersionCommand() {
    testCommandRegexMatches("--version", "pfb \\d+\\.\\d+\\.\\d\n");
  }

  @Test
  void testCorrectVersion() {
    GitConfiguration gitConfiguration = new GitConfiguration();
    String expectedVersion = gitConfiguration.getCliVersion();
    testCommandStringMatches("--version", "pfb " + expectedVersion + "\n");
  }

  @Test
  void testHelpCommand() {
    testCommandStringMatches("--help", "Usage: pfb");
  }

  private void testCommandStringMatches(String command, String expectedOutput) {
    testCommandStringMatches(List.of(command), expectedOutput);
  }

  private void testCommandStringMatches(List<String> commands, String expectedOutput) {
    testCommandStringMatches(commands, outContent, expectedOutput);
  }

  private void testCommandStringMatches(
      List<String> commands, ByteArrayOutputStream output, String expectedOutput) {
    executeCommand(commands);
    assertThat(output.toString(), containsStringIgnoringCase(expectedOutput));
  }

  private void testCommandRegexMatches(String command, String expectedOutput) {
    executeCommand(List.of(command));
    assertThat(outContent.toString(), matchesPattern(expectedOutput));
  }

  private void executeCommand(List<String> commands) {
    JavaPfbCommand.executeCommand(commands.toArray(new String[0]));
  }
}
