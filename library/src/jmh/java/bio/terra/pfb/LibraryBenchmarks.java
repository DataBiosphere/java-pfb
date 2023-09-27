package bio.terra.pfb;

import java.io.IOException;
import org.openjdk.jmh.annotations.Benchmark;

/**
 * JMH benchmarks for the Library class.
 *
 * <p>Benchmarks do not assert correctness; that's what unit tests are for. These exist only to
 * measure performance.
 */
public class LibraryBenchmarks {

  // benchmarks for the main "show nodes" method against a small data file
  @Benchmark
  public void showNodesSmall() throws IOException {
    Library.showNodes("src/jmh/resources/small.avro");
  }

  // benchmarks for the main "show nodes" method against a medium-sized data file
  @Benchmark
  public void showNodesMedium() throws IOException {
    Library.showNodes("src/jmh/resources/medium.avro");
  }
}
