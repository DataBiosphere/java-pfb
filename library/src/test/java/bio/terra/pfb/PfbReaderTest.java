package bio.terra.pfb;

import static bio.terra.pfb.utils.CompareOutputUtils.FileExtension.JSON;
import static bio.terra.pfb.utils.CompareOutputUtils.FileExtension.TXT;
import static bio.terra.pfb.utils.CompareOutputUtils.PfbCommandType.SHOW;
import static bio.terra.pfb.utils.CompareOutputUtils.PfbCommandType.SHOW_METADATA;
import static bio.terra.pfb.utils.CompareOutputUtils.PfbCommandType.SHOW_NODES;
import static bio.terra.pfb.utils.CompareOutputUtils.PfbCommandType.SHOW_SCHEMA;

import bio.terra.pfb.utils.CompareOutputUtils;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class PfbReaderTest {

  private static final List<String> listOfTestFiles =
      List.of("minimal_schema", "test", "kf", "data-with-array");

  @Test
  void showSchemaTest() throws IOException {
    for (String fileName : listOfTestFiles) {
      CompareOutputUtils.compareJavaPfbWithPyPfb(fileName, SHOW_SCHEMA, "", JSON);
    }
  }

  @Test
  void showNodesTest() throws IOException {
    var listOfTestFiles = List.of("minimal_data", "minimal_schema", "test", "kf");
    for (String fileName : listOfTestFiles) {
      CompareOutputUtils.compareJavaPfbWithPyPfb(fileName, SHOW_NODES, "", TXT);
    }
  }

  @Test
  void showMetadata() throws IOException {
    for (String fileName : listOfTestFiles) {
      CompareOutputUtils.compareJavaPfbWithPyPfb(fileName, SHOW_METADATA, "", JSON);
    }
  }

  @Test
  void showTest() throws IOException {
    // TODO - fix case described in testLongDecimalShow() to remove the following line of code
    var editedListOfTestFiles = listOfTestFiles.stream().filter(f -> !f.equals("test")).toList();
    for (String fileName : editedListOfTestFiles) {
      System.out.print("Testing file: " + fileName + "\n");
      CompareOutputUtils.compareJSONLineByLine(fileName, SHOW, "");
    }
  }

  // TODO
  // This test fails because pyPFB returns 13 decimal places (ex: 12.1218843460083)
  // But both the avro file and java-pfb return 6 decimal places (ex: 12.121884)
  @Disabled(
      "Disabled because the test file includes long numeric values that do not compare correctly between pyPFB and java-pfb.")
  @Test
  void testLongDecimalShow() throws IOException {
    CompareOutputUtils.compareJSONLineByLine("test", SHOW, "");
  }

  @Disabled("Disabled because we don't have a way to generate a signed URL for testing")
  @Test
  void testSignedURL() throws IOException {
    // NOTE: this is not a permanent URL, it will expire
    String signedUrl =
        "https://tdrshtikoojbfebzqfkvhyvi.blob.core.windows.net/04c9ecfe-e93d-4d92-929a-d4af7f429779/metadata/parquet/datarepo_row_ids/datarepo_row_ids.parquet/minimal_data.avro?sp=r&st=2023-08-07T17:55:54Z&se=2023-08-08T01:55:54Z&spr=https&sv=2022-11-02&sr=b&sig=LV4RkXMtXwwksYobDTuEHbdd8%2BLKJCxwtCs%2F09o1FYY%3D";
    CompareOutputUtils.compareJavaPfbWithPyPfb("minimal_data", SHOW_SCHEMA, signedUrl, JSON);
  }
}
