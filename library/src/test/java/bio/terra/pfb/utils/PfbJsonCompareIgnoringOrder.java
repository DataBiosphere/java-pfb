package bio.terra.pfb.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import java.util.Comparator;

class PfbJsonCompareIgnoringOrder implements Comparator<JsonNode> {
  @Override
  public int compare(JsonNode o1, JsonNode o2) {
    if (o1.equals(o2)) {
      return 0;
    }
    if ((o1 instanceof TextNode) && (o2 instanceof TextNode)) {
      String s1 = ((TextNode) o1).asText();
      String s2 = ((TextNode) o2).asText();

      if (s1.equalsIgnoreCase(s2)) {
        return 0;
      } else {
        System.out.println("Error: Unequal text nodes");
        System.out.println("s1: " + s1);
        System.out.println("s2: " + s2);
      }
    }
    return 1;
  }
}
