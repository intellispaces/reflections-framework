package tech.intellispaces.reflections.traverse;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link TraverseTypes}.
 */
public class TraverseTypesTest {

  @Test
  public void testOrdinal() {
    assertThat(TraverseTypes.Mapping.ordinal()).isEqualTo(0);
    assertThat(TraverseTypes.Moving.ordinal()).isEqualTo(1);
    assertThat(TraverseTypes.MappingOfMoving.ordinal()).isEqualTo(2);
  }
}
