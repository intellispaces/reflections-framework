package tech.intellispaces.jaquarius.object.reference;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link MovabilityTypes}.
 */
public class MobilityTypesTest {

  @Test
  public void testOrdinal() {
    assertThat(MovabilityTypes.Undefined.ordinal()).isEqualTo(0);
    assertThat(MovabilityTypes.Unmovable.ordinal()).isEqualTo(1);
    assertThat(MovabilityTypes.Movable.ordinal()).isEqualTo(2);
  }
}
