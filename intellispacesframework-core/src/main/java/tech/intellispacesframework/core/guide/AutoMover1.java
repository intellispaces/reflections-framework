package tech.intellispacesframework.core.guide;

import tech.intellispacesframework.core.exception.TraverseException;
import tech.intellispacesframework.core.guide.n1.Mover1;
import tech.intellispacesframework.core.traverseplan.TraversePlan;

import java.util.function.BiConsumer;

public class AutoMover1<S, Q> implements Mover1<S, Q> {
  private final TraversePlan taskPlan;

  public AutoMover1(TraversePlan taskPlan) {
    this.taskPlan = taskPlan;
  }

  @Override
  public S move(S source, Q qualifier) throws TraverseException {
    taskPlan.traverse(source, qualifier);
    return source;
  }

  @Override
  public BiConsumer<S, Q> asBiConsumer() {
    return (source, qualifier) -> {
      try {
        move(source, qualifier);
      } catch (TraverseException e) {
        throw new RuntimeException(e);
      }
    };
  }

  @Override
  public void async(S source, Object... qualifiers) {

  }
}
