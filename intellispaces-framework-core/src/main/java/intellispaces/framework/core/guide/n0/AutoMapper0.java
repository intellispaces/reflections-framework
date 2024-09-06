package intellispaces.framework.core.guide.n0;

import intellispaces.framework.core.exception.TraverseException;
import intellispaces.framework.core.traverse.DeclarativePlan;
import intellispaces.framework.core.traverse.TraverseExecutor;

/**
 * Not-parametrized automatic mapper guide.
 *
 * <p>Automatic guide builds the traverse plan itself.
 *
 * @param <S> source object handle type.
 * @param <T> target object handle type.
 */
public class AutoMapper0<S, T> implements AbstractMapper0<S, T> {
  private final String tid;
  private final TraverseExecutor traverseExecutor;
  private final DeclarativePlan declarativeTaskPlan;

  public AutoMapper0(String tid, DeclarativePlan declarativeTaskPlan, TraverseExecutor traverseExecutor) {
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
  public T map(S source) throws TraverseException {
    return (T) declarativeTaskPlan.execute(source, traverseExecutor);
  }
}
