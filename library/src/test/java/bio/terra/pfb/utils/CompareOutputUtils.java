package bio.terra.pfb.utils;

import static org.junit.jupiter.api.Assertions.assertTrue;

import bio.terra.pfb.PfbReader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CompareOutputUtils {

  public static void compareOutputWithPyPFB(
      String fileName, pfbCommandType commandType, String filePath) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    PfbReader reader = new PfbReader();

    try {
      String jsonString =
          Files.readString(
              Paths.get(
                  "src/test/resources/pyPfbOutput/" + commandType + "/" + fileName + ".json"));
      JsonNode pyPfbJsonOutput = mapper.readTree(jsonString);
      if (filePath.isEmpty()) {
        filePath = String.format("src/test/resources/avro/%s.avro", fileName);
      }
      String showSchemaOutput = reader.showSchema(filePath);
      JsonNode showSchemaOutputJson = mapper.readTree(showSchemaOutput);

      PfbJsonComparator cmp = new PfbJsonComparator();
      assertTrue(pyPfbJsonOutput.equals(cmp, showSchemaOutputJson));
    } catch (IOException e) {
      throw new IOException("Error reading file: " + fileName, e);
    }
  }

  public enum pfbCommandType {
    ShowSchema
  }
}
