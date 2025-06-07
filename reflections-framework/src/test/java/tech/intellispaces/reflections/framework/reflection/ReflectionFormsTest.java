package tech.intellispaces.reflections.framework.reflection;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ReflectionForms}.
 */
public class ReflectionFormsTest {

  @Test
  public void testOrdinal() {
    assertThat(ReflectionForms.Reflection.ordinal()).isEqualTo(0);
    assertThat(ReflectionForms.Primitive.ordinal()).isEqualTo(1);
    assertThat(ReflectionForms.PrimitiveWrapper.ordinal()).isEqualTo(2);
  }
}
