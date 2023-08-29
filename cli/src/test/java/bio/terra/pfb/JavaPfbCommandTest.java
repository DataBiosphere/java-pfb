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

  @ParameterizedTest
  @ValueSource(strings = {"nodes", "metadata", "schema", ""})
  void testShowCommand(String option) {
    testCommandStringMatches(List.of("show", option), option);
  }

  @Test
  void filePath() {
    testCommandStringMatches(List.of("show", "-i /path/to/file"), "path/to/file");
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
    executeCommand(commands, expectedOutput);
    assertThat(outContent.toString(), containsStringIgnoringCase(expectedOutput));
  }

  private void testCommandRegexMatches(String command, String expectedOutput) {
    executeCommand(List.of(command), expectedOutput);
    assertThat(outContent.toString(), matchesPattern(expectedOutput));
  }

  private void executeCommand(List<String> commands, String expectedOutput) {
    String[] args = new String[commands.size()];
    for (int i = 0; i < commands.size(); i++) args[i] = commands.get(i);
    JavaPfbCommand.executeCommand(args);
  }
}
