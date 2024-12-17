package tech.intellispaces.jaquarius.traverse.plan;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link TraversePlanTypes}.
 */
public class TraversePlanTypesTest {

  @Test
  public void testOrdinal() {
    assertThat(TraversePlanTypes.CallGuide0.ordinal()).isEqualTo(0);
    assertThat(TraversePlanTypes.CallGuide1.ordinal()).isEqualTo(1);
    assertThat(TraversePlanTypes.CallGuide2.ordinal()).isEqualTo(2);
    assertThat(TraversePlanTypes.CallGuide3.ordinal()).isEqualTo(3);
    assertThat(TraversePlanTypes.CallGuide4.ordinal()).isEqualTo(4);
    assertThat(TraversePlanTypes.MapObjectHandleThruChannel0.ordinal()).isEqualTo(5);
    assertThat(TraversePlanTypes.MapObjectHandleThruChannel1.ordinal()).isEqualTo(6);
    assertThat(TraversePlanTypes.MapObjectHandleThruChannel2.ordinal()).isEqualTo(7);
    assertThat(TraversePlanTypes.MapObjectHandleThruChannel3.ordinal()).isEqualTo(8);
    assertThat(TraversePlanTypes.MoveObjectHandleThruChannel0.ordinal()).isEqualTo(9);
    assertThat(TraversePlanTypes.MoveObjectHandleThruChannel1.ordinal()).isEqualTo(10);
    assertThat(TraversePlanTypes.MoveObjectHandleThruChannel2.ordinal()).isEqualTo(11);
    assertThat(TraversePlanTypes.MoveObjectHandleThruChannel3.ordinal()).isEqualTo(12);
    assertThat(TraversePlanTypes.MapOfMovingObjectHandleThruChannel0.ordinal()).isEqualTo(13);
    assertThat(TraversePlanTypes.MapOfMovingObjectHandleThruChannel1.ordinal()).isEqualTo(14);
    assertThat(TraversePlanTypes.MapOfMovingObjectHandleThruChannel2.ordinal()).isEqualTo(15);
    assertThat(TraversePlanTypes.MapOfMovingObjectHandleThruChannel3.ordinal()).isEqualTo(16);
    assertThat(TraversePlanTypes.MapOfMovingObjectHandleThruChannel4.ordinal()).isEqualTo(17);
  }
}
