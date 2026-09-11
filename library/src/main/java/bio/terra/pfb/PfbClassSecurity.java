package bio.terra.pfb;

import org.apache.avro.Schema;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.ClassSecurityValidator;

/**
 * Marks the classes generated from {@code PfbSchema} as trusted for Avro deserialization.
 *
 * <p>As of Avro 1.12.2 (<a href="https://issues.apache.org/jira/browse/AVRO-4189">AVRO-4189</a>),
 * {@code SpecificDatumReader} refuses to instantiate any class that has not been explicitly
 * allowlisted, failing with {@code SecurityException: Forbidden bio.terra.pfb.Entity! This class is
 * not trusted to be included in Avro schemas.} The generated PFB types - {@code Entity}, {@code
 * Metadata}, {@code Node} and friends - are the only classes this library asks Avro to instantiate,
 * so we trust exactly those rather than a broad wildcard via {@code
 * org.apache.avro.SERIALIZABLE_PACKAGES=*}.
 *
 * <p>{@link PfbReader} registers them automatically. Callers that read PFB files with their own
 * {@code SpecificDatumReader} over these generated classes should call {@link #register()} first.
 */
public final class PfbClassSecurity {

  private static boolean registered = false;

  private PfbClassSecurity() {}

  /**
   * Adds the generated PFB types to Avro's global allowlist. Idempotent, and safe to call from any
   * thread.
   */
  public static synchronized void register() {
    if (registered) {
      return;
    }

    ClassSecurityValidator.Builder trustedPfbTypes = ClassSecurityValidator.builder();
    for (Schema type : PfbSchema.PROTOCOL.getTypes()) {
      trustedPfbTypes.add(SpecificData.getClassName(type));
    }

    // Compose with the current validator instead of replacing it, so we don't overwrite
    // Avro's own defaults or anything already allowed through the
    // org.apache.avro.SERIALIZABLE_CLASSES / SERIALIZABLE_PACKAGES properties
    ClassSecurityValidator.setGlobal(
        ClassSecurityValidator.composite(
            ClassSecurityValidator.getGlobal(), trustedPfbTypes.build()));

    registered = true;
  }
}
