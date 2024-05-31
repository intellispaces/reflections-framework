package tech.intellispacesframework.core.guide;

import tech.intellispacesframework.core.exception.TraverseException;
import tech.intellispacesframework.core.traverse.DeclarativePlan;
import tech.intellispacesframework.core.traverse.TraverseExecutor;

/**
 * Not-parametrized automatic mover guide.
 *
 * <p>Automatic guide builds the traverse plan itself.
 *
 * @param <S> source object type.
 */
public class AutoMover0<S> extends AbstractMover0<S> {
  private final String tid;
  private final TraverseExecutor traverseExecutor;
  private final DeclarativePlan declarativeTaskPlan;

  public AutoMover0(String tid, DeclarativePlan declarativeTaskPlan, TraverseExecutor traverseExecutor) {
    this.tid = tid;
    this.declarativeTaskPlan = declarativeTaskPlan;
    this.traverseExecutor = traverseExecutor;
  }

  @Override
  public String tid() {
    return tid;
  }

  @Override
  @SuppressWarnings("unchecked")
  public S move(S source) throws TraverseException {
    return (S) declarativeTaskPlan.execute(source, traverseExecutor);
  }
}
