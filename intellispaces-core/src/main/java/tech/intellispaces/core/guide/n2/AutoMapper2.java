package tech.intellispaces.core.guide.n2;

import tech.intellispaces.core.exception.TraverseException;
import tech.intellispaces.core.traverse.DeclarativePlan;
import tech.intellispaces.core.traverse.TraverseExecutor;

/**
 * Two-parametrized automatic mapper guide.
 *
 * <p>Automatic guide builds the traverse plan itself.
 *
 * @param <S> source object handle type.
 * @param <T> target object handle type.
 * @param <Q1> first qualifier handle type.
 * @param <Q2> second qualifier handle type.
 */
public class AutoMapper2<S, T, Q1, Q2> implements AbstractMapper2<S, T, Q1, Q2> {
  private final String tid;
  private final TraverseExecutor traverseExecutor;
  private final DeclarativePlan declarativeTaskPlan;

  public AutoMapper2(String tid, DeclarativePlan declarativeTaskPlan, TraverseExecutor traverseExecutor) {
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
  public T map(S source, Q1 qualifier1, Q2 qualifier2) throws TraverseException {
    return (T) declarativeTaskPlan.execute(source, qualifier1, qualifier2, traverseExecutor);
  }
}
