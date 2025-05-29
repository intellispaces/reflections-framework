package tech.intellispaces.reflections.framework.traverse.plan;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link TraversePlanTypes}.
 */
public class TraversePlanTypesTest {

  @Test
  public void testOrdinal() {
    assertThat(TraversePlanTypes.CallLocalGuide0.ordinal()).isEqualTo(0);
    assertThat(TraversePlanTypes.CallLocalGuide1.ordinal()).isEqualTo(1);
    assertThat(TraversePlanTypes.CallLocalGuide2.ordinal()).isEqualTo(2);
    assertThat(TraversePlanTypes.CallLocalGuide3.ordinal()).isEqualTo(3);
    assertThat(TraversePlanTypes.CallLocalGuide4.ordinal()).isEqualTo(4);
    assertThat(TraversePlanTypes.MapSpecifiedClassSourceThruIdentifiedChannel0.ordinal()).isEqualTo(5);
    assertThat(TraversePlanTypes.MapSpecifiedClassSourceThruIdentifiedChannel1.ordinal()).isEqualTo(6);
    assertThat(TraversePlanTypes.MapSpecifiedClassSourceThruIdentifiedChannel2.ordinal()).isEqualTo(7);
    assertThat(TraversePlanTypes.MapSpecifiedClassSourceThruIdentifiedChannel3.ordinal()).isEqualTo(8);
    assertThat(TraversePlanTypes.MoveSpecifiedClassSourceThruIdentifiedChannel0.ordinal()).isEqualTo(9);
    assertThat(TraversePlanTypes.MoveSpecifiedClassSourceThruIdentifiedChannel1.ordinal()).isEqualTo(10);
    assertThat(TraversePlanTypes.MoveSpecifiedClassSourceThruIdentifiedChannel2.ordinal()).isEqualTo(11);
    assertThat(TraversePlanTypes.MoveSpecifiedClassSourceThruIdentifiedChannel3.ordinal()).isEqualTo(12);
    assertThat(TraversePlanTypes.MapOfMovingSpecifiedClassSourceThruIdentifiedChannel0.ordinal()).isEqualTo(13);
    assertThat(TraversePlanTypes.MapOfMovingSpecifiedClassSourceThruIdentifiedChannel1.ordinal()).isEqualTo(14);
    assertThat(TraversePlanTypes.MapOfMovingSpecifiedClassSourceThruIdentifiedChannel2.ordinal()).isEqualTo(15);
    assertThat(TraversePlanTypes.MapOfMovingSpecifiedClassSourceThruIdentifiedChannel3.ordinal()).isEqualTo(16);
    assertThat(TraversePlanTypes.MapOfMovingSpecifiedClassSourceThruIdentifiedChannel4.ordinal()).isEqualTo(17);
    assertThat(TraversePlanTypes.AscendAndExecute0.ordinal()).isEqualTo(18);
    assertThat(TraversePlanTypes.AscendAndExecute1.ordinal()).isEqualTo(19);
    assertThat(TraversePlanTypes.MapSpecifiedSourceToSpecifiedTargetDomainAndClass.ordinal()).isEqualTo(20);
  }
}
