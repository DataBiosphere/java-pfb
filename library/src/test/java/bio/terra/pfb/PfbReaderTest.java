package bio.terra.pfb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.avro.Schema;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;

class PfbReaderTest {

  @Test
  void testGetPFBSchemaFromClasspathFile() throws IOException {
    String expectedData = "Multiplicity";
    String filePath = "src/main/resources/avro/minimal_data.avro";
    PfbReader reader = new PfbReader();
    Schema schema = reader.readPFBSchema(filePath);
    List<Schema> pfbSchemaShow = new ArrayList<>();
    pfbSchemaShow.add(schema.getField("object").schema().getField("root").schema());
    pfbSchemaShow.add(schema.getField("object").schema().getField("data_release").schema());
    pfbSchemaShow.add(
        schema.getField("object").schema().getField("submitted_aligned_reads").schema());
    JSONArray jsonArray = new JSONArray(pfbSchemaShow);
  }

  //  @Test
  //  void testGetSchemaFromClasspathFile() throws IOException {
  //    String expectedData = "Multiplicity";
  //    String filePath = "src/main/resources/minimal_data.avro";
  //    PfbReader reader = new PfbReader();
  //    Schema schema = reader.readSchema(filePath);
  //    String mult = schema.toString(true);
  //  }
  //
  //  @Test
  //  void testReadClasspathFile() throws IOException {
  //    String expectedData = "Multiplicity";
  //    String filePath = "/avro/minimal_data.avro";
  //    PfbReader reader = new PfbReader();
  //    String data = reader.readPFBFile(filePath);
  //
  //    assertThat(data, containsString(expectedData));
  //  }
  //
  //  @Test
  //  void testSignedURL() throws IOException {
  //    String expectedData = "Multiplicity";
  //    // NOTE: this is not a permanent URL, it will expire
  //    String signedUrl =
  //
  // "https://tdrshtikoojbfebzqfkvhyvi.blob.core.windows.net/04c9ecfe-e93d-4d92-929a-d4af7f429779/metadata/parquet/datarepo_row_ids/datarepo_row_ids.parquet/minimal_data.avro?sp=r&st=2023-08-03T19:11:11Z&se=2023-08-04T03:11:11Z&spr=https&sv=2022-11-02&sr=b&sig=u63%2F2kj0JsKTcncfEKbswWcxW8VtwEyg4LfWWY3NDuM%3D";
  //
  //    PfbReader reader = new PfbReader();
  //    String data = reader.readPFBFile(signedUrl);
  //    assertThat(data, containsString(expectedData));
  //  }
}
