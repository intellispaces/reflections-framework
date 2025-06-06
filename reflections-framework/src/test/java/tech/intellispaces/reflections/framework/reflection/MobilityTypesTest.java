package tech.intellispaces.reflections.framework.reflection;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link MovabilityTypes}.
 */
public class MobilityTypesTest {

  @Test
  public void testOrdinal() {
    assertThat(MovabilityTypes.General.ordinal()).isEqualTo(0);
    assertThat(MovabilityTypes.Movable.ordinal()).isEqualTo(1);
  }
}
