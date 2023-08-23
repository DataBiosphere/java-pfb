package bio.terra.pfb;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LibraryTest {
  private static final String TEST_FILE_LOCATION = "path/to/file/minimal_schema.avro";
  private static final String FAKE_DATA_RETURN = "Fake data return";
  @Mock PfbReader pfbReader;

  @Test
  void showSchemaTest() throws IOException {
    when(pfbReader.showSchema(TEST_FILE_LOCATION)).thenReturn(FAKE_DATA_RETURN);
    Library library = new Library(pfbReader);
    assertReturnValueMatches(library.showSchema(TEST_FILE_LOCATION));
    verify(pfbReader).showSchema(TEST_FILE_LOCATION);
  }

  @Test
  void showMetadataTest() {
    when(pfbReader.showMetadata(TEST_FILE_LOCATION)).thenReturn(FAKE_DATA_RETURN);
    Library library = new Library(pfbReader);
    assertReturnValueMatches(library.showMetadata(TEST_FILE_LOCATION));
    verify(pfbReader).showMetadata(TEST_FILE_LOCATION);
  }

  @Test
  void showTableRowTest() {
    when(pfbReader.show(TEST_FILE_LOCATION))
        .thenReturn(Collections.singletonList(FAKE_DATA_RETURN));
    Library library = new Library(pfbReader);
    assertReturnValueMatches(library.showTableRows(TEST_FILE_LOCATION));
    verify(pfbReader).show(TEST_FILE_LOCATION);
  }

  @Test
  void showNodesTest() {
    when(pfbReader.showNodes(TEST_FILE_LOCATION)).thenReturn(FAKE_DATA_RETURN);
    Library library = new Library(pfbReader);
    assertReturnValueMatches(library.showNodes(TEST_FILE_LOCATION));
    verify(pfbReader).showNodes(TEST_FILE_LOCATION);
  }

  private void assertReturnValueMatches(String returnedVal) {
    assertThat(
        "Returned value should be the same as the mocked return value",
        returnedVal,
        containsString(FAKE_DATA_RETURN));
  }
}
