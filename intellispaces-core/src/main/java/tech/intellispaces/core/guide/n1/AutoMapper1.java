package tech.intellispaces.core.guide.n1;

import tech.intellispaces.core.exception.TraverseException;
import tech.intellispaces.core.traverse.DeclarativePlan;
import tech.intellispaces.core.traverse.TraverseExecutor;

/**
 * One-parametrized automatic mapper guide.
 *
 * <p>Automatic guide builds the traverse plan itself.
 *
 * @param <S> source object handle type.
 * @param <T> target object handle type.
 * @param <Q> qualifier handle type.
 */
public class AutoMapper1<S, T, Q> implements AbstractMapper1<S, T, Q> {
  private final String tid;
  private final TraverseExecutor traverseExecutor;
  private final DeclarativePlan declarativeTaskPlan;

  public AutoMapper1(String tid, DeclarativePlan declarativeTaskPlan, TraverseExecutor traverseExecutor) {
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
  public T map(S source, Q qualifier) throws TraverseException {
    return (T) declarativeTaskPlan.execute(source, qualifier, traverseExecutor);
  }
}
