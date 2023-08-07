package bio.terra.pfb.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import java.util.Comparator;
import java.util.List;

class PfbJsonComparator implements Comparator<JsonNode> {
  @Override
  public int compare(JsonNode o1, JsonNode o2) {
    if (o1.equals(o2)) {
      return 0;
    }
    if ((o1 instanceof TextNode) && (o2 instanceof TextNode)) {
      String s1 = ((TextNode) o1).asText();
      String s2 = ((TextNode) o2).asText();

      // TODO - figure out why we're seeing this strange case of the encoded underscore
      // I see this in the source avro file, but not in the output of the pyPFB show schema
      // command
      var fileSizeNames = List.of("file_size", "file_5f_size");
      var fileFormatNames = List.of("file_format", "file_5f_format");
      if (fileSizeNames.containsAll(List.of(s1, s2))
          || fileFormatNames.containsAll(List.of(s1, s2))
          || s1.equalsIgnoreCase(s2)) {
        return 0;
      }
    }
    return 1;
  }
}
