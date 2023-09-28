package bio.terra.pfb;

import org.openjdk.jmh.annotations.Benchmark;

/**
 * JMH benchmarks for the PfbReader class.
 *
 * <p>Benchmarks do not assert correctness; that's what unit tests are for. These exist only to
 * measure performance.
 */
public class PfbReaderBenchmarks {

  private static final String ENCODED_ENUM = "ADULT_20__28_25_2e_1_2d_33cm_29_";

  // benchmark for enum decoding
  @Benchmark
  public void convertEnum() {
    String actual = PfbReader.convertEnum(ENCODED_ENUM);
    assert(!actual.isEmpty());
  }
}
