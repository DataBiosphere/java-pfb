package bio.terra.pfb.models.commands;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class CommandInterfaceTest {

  @Test
  void showMetadata() throws IOException {
    testCommand(new ShowMetadata(), "nodes");
  }

  @Test
  void showNodes() throws IOException {
    testCommand(new ShowNodes(), "data_release");
  }

  @Test
  void showSchema() throws IOException {
    testCommand(new ShowSchema(), "schema_version");
  }

  @Test
  void showTableRows() throws IOException {
    testCommandWithLimit(new TableRows(), "HG01101_cram", 1);
  }

  void testCommandWithLimit(
      PfbLibraryCommandInterface commandInterface, String testString, int limit)
      throws IOException {
    String result = commandInterface.commandWithLimit(getTestFileAbsolutePath(), limit);
    assertThat(result, containsString(testString));
  }

  void testCommand(PfbLibraryCommandInterface commandInterface, String testString)
      throws IOException {
    String result = commandInterface.command(getTestFileAbsolutePath());
    assertThat(result, containsString(testString));
  }

  private String getTestFileAbsolutePath() {
    String path = "src/test/resources/avro/minimal_data.avro";
    File file = new File(path);
    return file.getAbsolutePath();
  }
}
