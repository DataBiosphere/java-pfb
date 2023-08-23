package bio.terra.pfb;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
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
  void run() {
    javaPfbCommand.run();
    assertThat(outContent.toString(), containsString("PFB RUN"));
  }

  @Test
  void showNodes() {
    String[] args = new String[2];
    args[0] = "show";
    args[1] = "nodes";
    javaPfbCommand.executeCommand(args);
    assertThat(outContent.toString(), containsString("show nodes"));
  }

  @Test
  void showMetadata() {
    String[] args = new String[2];
    args[0] = "show";
    args[1] = "metadata";
    javaPfbCommand.executeCommand(args);
    assertThat(outContent.toString(), containsString("show metadata"));
  }

  @Test
  void showSchema() {
    String[] args = new String[2];
    args[0] = "show";
    args[1] = "schema";
    javaPfbCommand.executeCommand(args);
    assertThat(outContent.toString(), containsString("Show schema"));
  }

  @Test
  void showTableRows() {
    String[] args = new String[1];
    args[0] = "show";
    javaPfbCommand.executeCommand(args);
    assertThat(outContent.toString(), containsString("show table rows"));
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
