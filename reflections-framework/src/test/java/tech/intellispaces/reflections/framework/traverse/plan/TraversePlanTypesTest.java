package tech.intellispaces.reflections.framework.traverse.plan;

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
    assertThat(TraversePlanTypes.MapThruChannel0.ordinal()).isEqualTo(5);
    assertThat(TraversePlanTypes.MapThruChannel1.ordinal()).isEqualTo(6);
    assertThat(TraversePlanTypes.MapThruChannel2.ordinal()).isEqualTo(7);
    assertThat(TraversePlanTypes.MapThruChannel3.ordinal()).isEqualTo(8);
    assertThat(TraversePlanTypes.MoveThruChannel0.ordinal()).isEqualTo(9);
    assertThat(TraversePlanTypes.MoveThruChannel1.ordinal()).isEqualTo(10);
    assertThat(TraversePlanTypes.MoveThruChannel2.ordinal()).isEqualTo(11);
    assertThat(TraversePlanTypes.MoveThruChannel3.ordinal()).isEqualTo(12);
    assertThat(TraversePlanTypes.MapOfMovingThruChannel0.ordinal()).isEqualTo(13);
    assertThat(TraversePlanTypes.MapOfMovingThruChannel1.ordinal()).isEqualTo(14);
    assertThat(TraversePlanTypes.MapOfMovingChannel2.ordinal()).isEqualTo(15);
    assertThat(TraversePlanTypes.MapOfMovingThruChannel3.ordinal()).isEqualTo(16);
    assertThat(TraversePlanTypes.MapOfMovingThruChannel4.ordinal()).isEqualTo(17);
    assertThat(TraversePlanTypes.AscendAndExecute0.ordinal()).isEqualTo(18);
    assertThat(TraversePlanTypes.AscendAndExecute1.ordinal()).isEqualTo(19);
  }
}
