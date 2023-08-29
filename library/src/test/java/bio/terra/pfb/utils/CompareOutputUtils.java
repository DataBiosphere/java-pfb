package bio.terra.pfb.utils;

import static bio.terra.pfb.utils.CompareOutputUtils.FileExtension.JSON;
import static bio.terra.pfb.utils.CompareOutputUtils.FileExtension.TXT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

import bio.terra.pfb.PfbReader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompareOutputUtils {
  private static final Logger logger = LoggerFactory.getLogger(CompareOutputUtils.class);

  public static void compareJavaPfbWithPyPfb(
      String fileName, PfbCommandType commandType, String filePath, FileExtension fileExtension)
      throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    PfbReader reader = new PfbReader();
    String pythonOutput;
    try {
      pythonOutput =
          Files.readString(
              Paths.get(getPyPfbOutputFilePath(fileName, commandType, filePath, fileExtension)));
    } catch (IOException e) {
      throw new IOException("Error reading file: " + fileName, e);
    }
    String avroFilePath = getAvroFilePath(fileName, filePath);
    String javaPfbOutput = "";
    switch (commandType) {
      case SHOW_SCHEMA:
        javaPfbOutput = reader.showSchema(avroFilePath);
        break;
      case SHOW_METADATA:
        javaPfbOutput = reader.showMetadata(avroFilePath);
        break;
      case SHOW_NODES:
        javaPfbOutput = reader.showNodes(avroFilePath);
        break;
      default:
        throw new InvalidObjectException("Invalid command type");
    }

    if (fileExtension.equals(JSON)) {
      JsonNode pyPfbJsonOutput = mapper.readTree(pythonOutput);
      JsonNode showSchemaOutputJson = mapper.readTree(javaPfbOutput);
      PfbJsonCompareIgnoringOrder cmp = new PfbJsonCompareIgnoringOrder();
      assertTrue(pyPfbJsonOutput.equals(cmp, showSchemaOutputJson));
      //      assertThat(pyPfbJsonOutput, equalTo(showSchemaOutputJson));
    } else if (fileExtension.equals(TXT)) {
      assertThat(pythonOutput, equalTo(javaPfbOutput));
    } else {
      throw new InvalidObjectException("Invalid file extension");
    }
  }

  public static void compareJSONLineByLine(
      String fileName, PfbCommandType commandType, String filePath) throws IOException {
    PfbReader reader = new PfbReader();
    ObjectMapper mapper = new ObjectMapper();

    String avroFilePath = getAvroFilePath(fileName, filePath);
    List<String> javaOutput = reader.show(avroFilePath);
    try {
      File file = new File(getPyPfbOutputFilePath(fileName, commandType, filePath, JSON));
      FileReader fileReader = new FileReader(file);
      BufferedReader bufferedReader = new BufferedReader(fileReader);
      String line;
      int count = 0;
      while ((line = bufferedReader.readLine()) != null) {
        JsonNode pyPfbJsonOutput = mapper.readTree(line);
        JsonNode javaPfbOutput = mapper.readTree(javaOutput.get(count));
        logger.info("Comparing line {}", count);
        logger.info("pyPfbJsonOutput: {}", pyPfbJsonOutput);
        logger.info("javaPfbOutput: {}", javaPfbOutput);
        assertThat(pyPfbJsonOutput, equalTo(javaPfbOutput));
        count++;
      }
      fileReader.close();
    } catch (IOException e) {
      logger.info("Error reading file: {}", fileName);
    }
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
      String fileName, PfbCommandType commandType, String filePath, FileExtension fileExtension) {
    if (filePath.isEmpty()) {
      filePath =
          String.format(
              "src/test/resources/pyPfbOutput/%s/%s.%s", commandType, fileName, fileExtension);
    }
    return filePath;
  }
}
