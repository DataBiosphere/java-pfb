package bio.terra.pfb.utils;

import static bio.terra.pfb.utils.CompareOutputUtils.FileExtension.JSON;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import bio.terra.pfb.PfbReader;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompareOutputUtils {
  private static final Logger logger = LoggerFactory.getLogger(CompareOutputUtils.class);

  public static void assertJavaPfbIsPyPFB(
      String fileName, PfbCommandType commandType, String filePath, FileExtension fileExtension)
      throws IOException {
    String pythonOutput;
    pythonOutput =
        Files.readString(Paths.get(getPyPfbOutputFilePath(fileName, commandType, fileExtension)));
    String avroFilePath = getAvroFilePath(fileName, filePath);
    String javaPfbOutput =
        switch (commandType) {
          case SHOW_SCHEMA -> PfbReader.showSchema(avroFilePath);
          case SHOW_METADATA -> PfbReader.showMetadata(avroFilePath);
          case SHOW_NODES -> PfbReader.showNodes(avroFilePath);
          default -> throw new InvalidObjectException("Invalid command type");
        };

    switch (fileExtension) {
      case JSON -> JSONAssert.assertEquals(pythonOutput, javaPfbOutput, true);
      case TXT -> assertThat(pythonOutput, equalTo(javaPfbOutput));
      default -> throw new InvalidObjectException("Invalid file extension");
    }
  }

  public static void compareJSONLineByLine(
      String fileName, PfbCommandType commandType, String filePath) throws IOException {
    String avroFilePath = getAvroFilePath(fileName, filePath);
    List<String> javaOutput = PfbReader.show(avroFilePath);
    Path file = Paths.get(getPyPfbOutputFilePath(fileName, commandType, JSON));
    List<String> lines = Files.readAllLines(file);
    for (int i = 0; i < lines.size(); i++) {
      JSONAssert.assertEquals(lines.get(i), javaOutput.get(i), true);
    }
  }

  public static void testDataStream(String fileName) throws IOException {
    String avroFilePath = getAvroFilePath(fileName, "");
    assertNotNull(PfbReader.getGenericRecordsStream(avroFilePath));
  }

  public enum PfbCommandType {
    SHOW_SCHEMA("showSchema"),
    SHOW_METADATA("showMetadata"),
    SHOW_NODES("showNodes"),
    SHOW("show");

    private final String displayName;

    PfbCommandType(String displayName) {
      this.displayName = displayName;
    }

    @Override
    public String toString() {
      return this.displayName;
    }
  }

  public enum FileExtension {
    JSON("json"),
    TXT("txt");

    private final String displayName;

    FileExtension(String displayName) {
      this.displayName = displayName;
    }

    @Override
    public String toString() {
      return this.displayName;
    }
  }

  private static String getAvroFilePath(String fileName, String filePath) {
    if (filePath.isEmpty()) {
      filePath = String.format("src/test/resources/avro/%s.avro", fileName);
    }
    return filePath;
  }

  private static String getPyPfbOutputFilePath(
      String fileName, PfbCommandType commandType, FileExtension fileExtension) {
    return String.format(
        "src/test/resources/pyPfbOutput/%s/%s.%s", commandType, fileName, fileExtension);
  }
}
