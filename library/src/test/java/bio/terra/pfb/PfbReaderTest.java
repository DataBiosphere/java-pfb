package bio.terra.pfb;

import static bio.terra.pfb.utils.CompareOutputUtils.FileExtension.JSON;
import static bio.terra.pfb.utils.CompareOutputUtils.FileExtension.TXT;
import static bio.terra.pfb.utils.CompareOutputUtils.PfbCommandType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import bio.terra.pfb.exceptions.InvalidPfbException;
import bio.terra.pfb.utils.CompareOutputUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.avro.Schema;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class PfbReaderTest {
  private static final Logger logger = LoggerFactory.getLogger(PfbReaderTest.class);
  private static final List<String> listOfTestFiles =
      List.of("minimal_schema", "test", "kf", "data-with-array", "empty");

  public static Stream<Arguments> provideTestFiles() {
    return listOfTestFiles.stream().map(Arguments::of);
  }

  @ParameterizedTest
  @MethodSource("provideTestFiles")
  void showSchemaTest(String fileName) throws IOException {
    // TODO - remove when fix is added for AJ-1288
    if (fileName.equals("empty")) {
      logger.error("Skipping test file: {} until fixed in AJ-1288\n", fileName);
    } else {
      CompareOutputUtils.assertJavaPfbIsPyPFB(fileName, SHOW_SCHEMA, "", JSON);
    }
  }

  // this test validates behavior of the PfbReader.getPfbSchema() method. This method is
  // used internally by PfbReader.showSchema, which is thoroughly tested by {@link
  // #showsSchemaTest()} therefore,
  // this test only performs cursory correctness checks.
  @ParameterizedTest
  @MethodSource("provideTestFiles")
  void getPfbSchemaTest(String fileName) throws IOException {
    // TODO - remove when fix is added for AJ-1288
    if (fileName.equals("empty")) {
      logger.error("Skipping test file: {} until fixed in AJ-1288\n", fileName);
    } else {
      // read the pypfb output for this file
      String expectedStr = CompareOutputUtils.getPyPfbOutput(fileName, SHOW_SCHEMA, JSON);
      // parse the pypfb output for this file
      ObjectMapper mapper = new ObjectMapper();
      JsonNode expected = mapper.readTree(expectedStr);
      // find the names of all top-level types from the pypfb output
      List<String> expectedNames =
          StreamSupport.stream(
                  Spliterators.spliteratorUnknownSize(expected.elements(), Spliterator.ORDERED),
                  false)
              .map(typeNode -> typeNode.get("name").asText())
              .toList();
      // get the PfbReader-calculated schema for this file from PfbReader
      List<Schema> actualSchema =
          PfbReader.getPfbSchema(CompareOutputUtils.getAvroFilePath(fileName, ""));
      // find the names of all top-level types in the actual schema
      List<String> actualNames = actualSchema.stream().map(Schema::getName).toList();

      assertEquals(expectedNames, actualNames);
    }
  }

  @ParameterizedTest
  @MethodSource("provideTestFiles")
  void showNodesTest(String fileName) throws IOException {
    CompareOutputUtils.assertJavaPfbIsPyPFB(fileName, SHOW_NODES, "", TXT);
  }

  @ParameterizedTest
  @MethodSource("provideTestFiles")
  void showMetadata(String fileName) throws IOException {
    CompareOutputUtils.assertJavaPfbIsPyPFB(fileName, SHOW_METADATA, "", JSON);
  }

  @ParameterizedTest
  @MethodSource("provideTestFiles")
  void getGenericRecordsStream(String fileName) throws IOException {
    CompareOutputUtils.testDataStream(fileName);
  }

  @Test
  void getGenericRecordsStreamError() {
    assertThrows(
        InvalidPfbException.class,
        () -> CompareOutputUtils.testDataStream("noFile.txt"),
        "Error reading PFB Value object");
  }

  @ParameterizedTest
  @MethodSource("provideTestFiles")
  void showTest(String fileName) throws IOException {
    // TODO - remove when fix is added for AJ-1292
    if (fileName.equals("test")) {
      logger.error("Skipping test file: {} until fixed in AJ-1292\n", fileName);
    } else {
      CompareOutputUtils.compareJSONLineByLine(fileName, SHOW, "");
    }
  }

  // TODO - fix in AJ-1292
  // This test fails because pyPFB returns 13 decimal places (ex: 12.1218843460083)
  // But both the avro file and java-pfb return 6 decimal places (ex: 12.121884)
  @Disabled(
      "Disabled because the test file includes long numeric values that do not compare correctly between pyPFB and java-pfb.")
  @Test
  void testLongDecimalShow() throws IOException {
    CompareOutputUtils.compareJSONLineByLine("test", SHOW, "");
  }

  // TODO - fix in AJ-1288
  // This test case fails because our workaround for handling enum encoding doesn't work with
  // special cases
  // Current workaround doesn't work for entries that contain sequential special characters and
  // numbers
  // Example:
  // before decoding - ADULT_20__28_25_2e_1_2d_33cm_29_
  // Actual decoded value - ADULT (25.1-33cm)
  // Incorrect value returned from this method - ADULT _28%2e_1-33cm)
  @Disabled(
      "Disabled because the test file includes enum values that do not compare correctly between pyPFB and java-pfb.")
  @Test
  void testEnumEncoding() throws IOException {
    CompareOutputUtils.assertJavaPfbIsPyPFB("empty", SHOW_SCHEMA, "", JSON);
  }

  @Disabled("Disabled because we don't have a way to generate a signed URL for testing")
  @Test
  void testSignedURL() throws IOException {
    String testFileName = "minimal_data";
    // NOTE: this is not a permanent URL, it will expire
    String signedUrl =
        "https://tdrshtikoojbfebzqfkvhyvi.blob.core.windows.net/04c9ecfe-e93d-4d92-929a-d4af7f429779/metadata/parquet/datarepo_row_ids/datarepo_row_ids.parquet/minimal_data.avro?sp=r&st=2023-09-01T13:40:20Z&se=2023-09-01T21:40:20Z&spr=https&sv=2022-11-02&sr=b&sig=%2Bh6g%2FbsyhsFcYv%2FJYWyBH5fEwPEeTSdhwDjHnshblxQ%3D";

    CompareOutputUtils.assertJavaPfbIsPyPFB(testFileName, SHOW_SCHEMA, signedUrl, JSON);
    CompareOutputUtils.assertJavaPfbIsPyPFB(testFileName, SHOW_NODES, signedUrl, TXT);
    CompareOutputUtils.compareJSONLineByLine(testFileName, SHOW, signedUrl);
    CompareOutputUtils.assertJavaPfbIsPyPFB(testFileName, SHOW_METADATA, signedUrl, JSON);
  }
}
