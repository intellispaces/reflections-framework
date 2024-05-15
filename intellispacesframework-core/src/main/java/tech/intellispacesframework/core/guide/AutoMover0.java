package tech.intellispacesframework.core.guide;

import tech.intellispacesframework.core.exception.TraverseException;
import tech.intellispacesframework.core.traverse.DeclarativeTraversePlan;
import tech.intellispacesframework.core.traverse.TraverseExecutor;

/**
 * Not-parametrized automatic mover guide.
 *
 * <p>Automatic guide builds the traverse plan itself.
 *
 * @param <S> source object type.
 */
public class AutoMover0<S> extends AbstractMover0<S> {
  private final TraverseExecutor traverseExecutor;
  private final DeclarativeTraversePlan declarativeTaskPlan;

  public AutoMover0(DeclarativeTraversePlan declarativeTaskPlan, TraverseExecutor traverseExecutor) {
    this.declarativeTaskPlan = declarativeTaskPlan;
    this.traverseExecutor = traverseExecutor;
  }

  @Override
  @SuppressWarnings("unchecked")
  public S move(S source) throws TraverseException {
    return (S) declarativeTaskPlan.execute(source, traverseExecutor);
  }
}
