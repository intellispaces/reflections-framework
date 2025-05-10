package tech.intellispaces.reflections.framework.object.reference;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ObjectReferenceForms}.
 */
public class ObjectReferenceFormTest {

  @Test
  public void testOrdinal() {
    assertThat(ObjectReferenceForms.Regular.ordinal()).isEqualTo(0);
    assertThat(ObjectReferenceForms.ObjectHandle.ordinal()).isEqualTo(1);
    assertThat(ObjectReferenceForms.Primitive.ordinal()).isEqualTo(2);
    assertThat(ObjectReferenceForms.PrimitiveWrapper.ordinal()).isEqualTo(3);
  }
}
