package bio.terra.pfb;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;
import org.apache.avro.Schema;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.ClassSecurityValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class PfbClassSecurityTest {

  @BeforeAll
  static void register() {
    PfbClassSecurity.register();
  }

  public static Stream<Class<?>> provideGeneratedTypes() {
    return PfbSchema.PROTOCOL.getTypes().stream().map(PfbClassSecurityTest::loadGeneratedClass);
  }

  /** Every type in pfbSchema.avdl must be instantiable by Avro's SpecificDatumReader. */
  @ParameterizedTest
  @MethodSource("provideGeneratedTypes")
  void generatedTypesAreTrusted(Class<?> generatedType) {
    assertDoesNotThrow(() -> ClassSecurityValidator.validate(generatedType));
  }

  /** Registering our types must not trust everything else along with them. */
  @Test
  void otherClassesRemainForbidden() {
    assertThrows(SecurityException.class, () -> ClassSecurityValidator.validate(PfbReader.class));
  }

  private static Class<?> loadGeneratedClass(Schema type) {
    try {
      return Class.forName(SpecificData.getClassName(type));
    } catch (ClassNotFoundException e) {
      throw new IllegalStateException("No generated class for schema " + type.getFullName(), e);
    }
  }
}
