package bio.terra.pfb;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.containsString;

import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LibraryTest {
  private static final String TEST_FILE_LOCATION = "path/to/file/minimal_schema.avro";
  private static final String FAKE_DATA_RETURN = "Fake data return";

  @Test
  void showSchemaTest() throws IOException {
    try (MockedStatic<PfbReader> utilities = Mockito.mockStatic(PfbReader.class)) {
      utilities.when(() -> PfbReader.showSchema(TEST_FILE_LOCATION)).thenReturn(FAKE_DATA_RETURN);
      assertReturnValueMatches(Library.showSchema(TEST_FILE_LOCATION));
    }
  }

  @Test
  void showMetadataTest() throws IOException {
    try (MockedStatic<PfbReader> utilities = Mockito.mockStatic(PfbReader.class)) {
      utilities.when(() -> PfbReader.showMetadata(TEST_FILE_LOCATION)).thenReturn(FAKE_DATA_RETURN);
      assertReturnValueMatches(Library.showMetadata(TEST_FILE_LOCATION));
    }
  }

  @Test
  void showTableRowTest() throws IOException {
    try (MockedStatic<PfbReader> utilities = Mockito.mockStatic(PfbReader.class)) {
      utilities
          .when(() -> PfbReader.show(TEST_FILE_LOCATION))
          .thenReturn(List.of(FAKE_DATA_RETURN));
      assertReturnValueMatches(Library.showTableRows(TEST_FILE_LOCATION));
    }
  }

  @Test
  void showLimitedTableRowTest() throws IOException {
    try (MockedStatic<PfbReader> utilities = Mockito.mockStatic(PfbReader.class)) {
      utilities
          .when(() -> PfbReader.show(TEST_FILE_LOCATION))
          .thenReturn(List.of(FAKE_DATA_RETURN, FAKE_DATA_RETURN, FAKE_DATA_RETURN));

      String limitedShow = Library.showTableRows(TEST_FILE_LOCATION, 2);
      testCorrectNumberReturned(limitedShow, 2);
      assertReturnValueMatches(limitedShow);

      String oneElementLimitedShow = Library.showTableRows(TEST_FILE_LOCATION, 1);
      testCorrectNumberReturned(oneElementLimitedShow, 1);
      String threeElementLimitedShow = Library.showTableRows(TEST_FILE_LOCATION, 3);
      testCorrectNumberReturned(threeElementLimitedShow, 3);
    }
  }

  private void testCorrectNumberReturned(String returnedVal, int expectedNum) {
    assertThat(
        "Correct number of elements returned", returnedVal.split("\n"), arrayWithSize(expectedNum));
  }

  @Test
  void showNodesTest() throws IOException {
    try (MockedStatic<PfbReader> utilities = Mockito.mockStatic(PfbReader.class)) {
      utilities.when(() -> PfbReader.showNodes(TEST_FILE_LOCATION)).thenReturn(FAKE_DATA_RETURN);
      assertReturnValueMatches(Library.showNodes(TEST_FILE_LOCATION));
    }
  }

  private void assertReturnValueMatches(String returnedVal) {
    assertThat(
        "Returned value should be the same as the mocked return value",
        returnedVal,
        containsString(FAKE_DATA_RETURN));
  }
}
