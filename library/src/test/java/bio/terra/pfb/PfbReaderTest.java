package bio.terra.pfb;

import bio.terra.pfb.utils.CompareOutputUtils;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class PfbReaderTest {

  @Test
  void testMinimalSchemaFiles() throws IOException {
    var listOfTestFiles = List.of("minimal_data", "minimal_schema", "kf");
    for (String fileName : listOfTestFiles) {
      CompareOutputUtils.compareOutputWithPyPFB(
          fileName, CompareOutputUtils.pfbCommandType.ShowSchema, "");
    }
  }

  @Disabled("Disabled because we don't have a way to generate a signed URL for testing")
  @Test
  void testSignedURL() throws IOException {
    // NOTE: this is not a permanent URL, it will expire
    String signedUrl =
        "https://tdrshtikoojbfebzqfkvhyvi.blob.core.windows.net/04c9ecfe-e93d-4d92-929a-d4af7f429779/metadata/parquet/datarepo_row_ids/datarepo_row_ids.parquet/minimal_data.avro?sp=r&st=2023-08-07T17:55:54Z&se=2023-08-08T01:55:54Z&spr=https&sv=2022-11-02&sr=b&sig=LV4RkXMtXwwksYobDTuEHbdd8%2BLKJCxwtCs%2F09o1FYY%3D";
    CompareOutputUtils.compareOutputWithPyPFB(
        "minimal_data", CompareOutputUtils.pfbCommandType.ShowSchema, signedUrl);
  }
}
