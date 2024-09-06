package intellispaces.framework.core.guide.n2;

import intellispaces.framework.core.exception.TraverseException;
import intellispaces.framework.core.traverse.DeclarativePlan;
import intellispaces.framework.core.traverse.TraverseExecutor;

/**
 * Two-parametrized automatic mover guide.
 *
 * <p>Automatic guide builds the traverse plan itself.
 *
 * @param <S> source object handle type.
 * @param <B> backward object handle type.
 * @param <Q1> first qualifier handle type.
 * @param <Q2> second qualifier handle type.
 */
public class AutoMover2<S, B, Q1, Q2> implements AbstractMover2<S, B, Q1, Q2> {
  private final String tid;
  private final TraverseExecutor traverseExecutor;
  private final DeclarativePlan declarativeTaskPlan;

  public AutoMover2(String tid, DeclarativePlan declarativeTaskPlan, TraverseExecutor traverseExecutor) {
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
  public B move(S source, Q1 qualifier1, Q2 qualifier2) throws TraverseException {
    return (B) declarativeTaskPlan.execute(source, qualifier1, qualifier2, traverseExecutor);
  }
}
