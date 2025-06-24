package tech.intellispaces.reflections.framework.traverse.plan;

import org.junit.jupiter.api.Test;

import tech.intellispaces.reflections.framework.task.plan.TaskPlanTypes;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link TaskPlanTypes}.
 */
public class TaskPlanTypesTest {

  @Test
  public void testOrdinal() {
    assertThat(TaskPlanTypes.CallLocalGuide.ordinal()).isEqualTo(0);
    assertThat(TaskPlanTypes.MapSourceSpecifiedClassThruIdentifiedChannel.ordinal()).isEqualTo(1);
    assertThat(TaskPlanTypes.MoveSourceSpecifiedClassThruIdentifiedChannel.ordinal()).isEqualTo(2);
    assertThat(TaskPlanTypes.MapOfMovingSourceSpecifiedClassThruIdentifiedChannel.ordinal()).isEqualTo(3);
    assertThat(TaskPlanTypes.AscendAndExecute.ordinal()).isEqualTo(4);
    assertThat(TaskPlanTypes.MapSpecifiedSourceToSpecifiedTargetDomainAndClass.ordinal()).isEqualTo(5);
  }
}
