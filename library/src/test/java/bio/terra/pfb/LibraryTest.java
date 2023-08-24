package bio.terra.pfb;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
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
  void showLimitedTableRowTest() {
    when(pfbReader.show(TEST_FILE_LOCATION))
        .thenReturn(List.of(FAKE_DATA_RETURN, FAKE_DATA_RETURN, FAKE_DATA_RETURN));
    Library library = new Library(pfbReader);

    String limitedShow = library.showTableRows(TEST_FILE_LOCATION, 2);
    testCorrectNumberReturned(limitedShow, 2);
    assertReturnValueMatches(limitedShow);
    verify(pfbReader).show(TEST_FILE_LOCATION);

    String oneElementLimitedShow = library.showTableRows(TEST_FILE_LOCATION, 1);
    testCorrectNumberReturned(oneElementLimitedShow, 1);
    String threeElementLimitedShow = library.showTableRows(TEST_FILE_LOCATION, 3);
    testCorrectNumberReturned(threeElementLimitedShow, 3);
  }

  private void testCorrectNumberReturned(String returnedVal, int expectedNum) {
    List<String> elements = Arrays.stream(returnedVal.split("\n")).toList();
    assertThat("Correct number of elements returned", elements.size(), equalTo(expectedNum));
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
