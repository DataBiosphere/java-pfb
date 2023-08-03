package bio.terra.pfb;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import java.io.IOException;
import org.junit.jupiter.api.Test;

class PfbReaderTest {
  @Test
  void testReadClasspathFile() throws IOException {
    String expectedData = "Multiplicity";
    String filePath = "/minimal_data.avro";
    PfbReader reader = new PfbReader();
    String data = reader.readPFBFile(filePath);

    assertThat(data, containsString(expectedData));
  }

  @Test
  void testSignedURL() throws IOException {
    String expectedData = "Multiplicity";
    // NOTE: this is not a permanent URL, it will expire
    String signedUrl =
        "https://tdrshtikoojbfebzqfkvhyvi.blob.core.windows.net/04c9ecfe-e93d-4d92-929a-d4af7f429779/metadata/parquet/datarepo_row_ids/datarepo_row_ids.parquet/minimal_data.avro?sp=r&st=2023-08-03T19:11:11Z&se=2023-08-04T03:11:11Z&spr=https&sv=2022-11-02&sr=b&sig=u63%2F2kj0JsKTcncfEKbswWcxW8VtwEyg4LfWWY3NDuM%3D";

    PfbReader reader = new PfbReader();
    String data = reader.readPFBFile(signedUrl);
    assertThat(data, containsString(expectedData));
  }
}
