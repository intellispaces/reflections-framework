package tech.intellispaces.jaquarius.object.reference;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ObjectForms}.
 */
public class ObjectFormTest {

  @Test
  public void testOrdinal() {
    assertThat(ObjectForms.Simple.ordinal()).isEqualTo(0);
    assertThat(ObjectForms.ObjectHandle.ordinal()).isEqualTo(1);
    assertThat(ObjectForms.Primitive.ordinal()).isEqualTo(2);
    assertThat(ObjectForms.PrimitiveWrapper.ordinal()).isEqualTo(3);
  }
}
