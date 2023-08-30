package bio.terra.pfb;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
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
  }

  @Test
  void showMetadataTest() throws IOException {
    when(pfbReader.showMetadata(TEST_FILE_LOCATION)).thenReturn(FAKE_DATA_RETURN);
    Library library = new Library(pfbReader);
    assertReturnValueMatches(library.showMetadata(TEST_FILE_LOCATION));
  }

  @Test
  void showTableRowTest() throws IOException {
    when(pfbReader.show(TEST_FILE_LOCATION))
        .thenReturn(Collections.singletonList(FAKE_DATA_RETURN));
    Library library = new Library(pfbReader);
    assertReturnValueMatches(library.showTableRows(TEST_FILE_LOCATION));
  }

  @Test
  void showLimitedTableRowTest() throws IOException {
    when(pfbReader.show(TEST_FILE_LOCATION))
        .thenReturn(List.of(FAKE_DATA_RETURN, FAKE_DATA_RETURN, FAKE_DATA_RETURN));
    Library library = new Library(pfbReader);

    String limitedShow = library.showTableRows(TEST_FILE_LOCATION, 2);
    testCorrectNumberReturned(limitedShow, 2);
    assertReturnValueMatches(limitedShow);

    String oneElementLimitedShow = library.showTableRows(TEST_FILE_LOCATION, 1);
    testCorrectNumberReturned(oneElementLimitedShow, 1);
    String threeElementLimitedShow = library.showTableRows(TEST_FILE_LOCATION, 3);
    testCorrectNumberReturned(threeElementLimitedShow, 3);
  }

  private void testCorrectNumberReturned(String returnedVal, int expectedNum) {
    assertThat(
        "Correct number of elements returned", returnedVal.split("\n"), arrayWithSize(expectedNum));
  }

  @Test
  void showNodesTest() throws IOException {
    when(pfbReader.showNodes(TEST_FILE_LOCATION)).thenReturn(FAKE_DATA_RETURN);
    Library library = new Library(pfbReader);
    assertReturnValueMatches(library.showNodes(TEST_FILE_LOCATION));
  }

  private void assertReturnValueMatches(String returnedVal) {
    assertThat(
        "Returned value should be the same as the mocked return value",
        returnedVal,
        containsString(FAKE_DATA_RETURN));
  }
}
