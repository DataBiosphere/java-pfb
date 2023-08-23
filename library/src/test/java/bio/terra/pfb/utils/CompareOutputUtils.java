package bio.terra.pfb.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import bio.terra.pfb.PfbReader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class CompareOutputUtils {

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
      case showSchema:
        javaPfbOutput = reader.showSchema(avroFilePath);
        break;
      case showMetadata:
        javaPfbOutput = reader.showMetadata(avroFilePath);
        break;
      case showNodes:
        javaPfbOutput = reader.showNodes(avroFilePath);
        break;
      default:
        throw new InvalidObjectException("Invalid command type");
    }

    if (fileExtension.equals(FileExtension.json)) {
      JsonNode pyPfbJsonOutput = mapper.readTree(pythonOutput);
      JsonNode showSchemaOutputJson = mapper.readTree(javaPfbOutput);
      assertThat(pyPfbJsonOutput, equalTo(showSchemaOutputJson));
    } else if (fileExtension.equals(FileExtension.txt)) {
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
      File file =
          new File(getPyPfbOutputFilePath(fileName, commandType, filePath, FileExtension.json));
      FileReader fileReader = new FileReader(file);
      BufferedReader bufferedReader = new BufferedReader(fileReader);
      String line;
      int count = 0;
      while ((line = bufferedReader.readLine()) != null) {
        JsonNode pyPfbJsonOutput = mapper.readTree(line);
        JsonNode javaPfbOutput = mapper.readTree(javaOutput.get(count));
        System.out.println("Comparing line " + count);
        System.out.println("pyPfbJsonOutput: " + pyPfbJsonOutput);
        System.out.println("javaPfbOutput: " + javaPfbOutput);
        assertThat(pyPfbJsonOutput, equalTo(javaPfbOutput));
        count++;
      }
      fileReader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public enum PfbCommandType {
    showSchema,
    show,
    showMetadata,
    showNodes
  }

  public enum FileExtension {
    json,
    txt
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