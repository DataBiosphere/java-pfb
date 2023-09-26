package bio.terra.pfb;

import static bio.terra.pfb.PfbReader.convertEnum;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Library {

  public static String showSchema(String fileLocation) throws IOException {
    return PfbReader.showSchema(fileLocation);
  }

  public static String showNodes(String fileLocation) throws IOException {
    return PfbReader.showNodes(fileLocation);
  }

  public static String showMetadata(String fileLocation) throws IOException {
    return PfbReader.showMetadata(fileLocation);
  }

  public static String showTableRows(String fileLocation) throws IOException {
    var records = PfbReader.getGenericRecords(fileLocation);
    ArrayList<String> data = new ArrayList<>();
    for (var record : records) {
      data.add(convertEnum(record.toString()));
    }

    return String.join("\n", data);
  }

  public static String showTableRows(String fileLocation, int limit) throws IOException {
    return PfbReader.show(fileLocation).stream().limit(limit).collect(Collectors.joining("\n"));
  }
}
