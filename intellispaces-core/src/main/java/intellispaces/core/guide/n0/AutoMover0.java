package intellispaces.core.guide.n0;

import intellispaces.core.exception.TraverseException;
import intellispaces.core.traverse.DeclarativePlan;
import intellispaces.core.traverse.TraverseExecutor;

/**
 * Not-parametrized automatic mover guide.
 *
 * <p>Automatic guide builds the traverse plan itself.
 *
 * @param <S> source object handle type.
 * @param <B> backward object handle type.
 */
public class AutoMover0<S, B> implements AbstractMover0<S, B> {
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
  public B move(S source) throws TraverseException {
    return (B) declarativeTaskPlan.execute(source, traverseExecutor);
  }
}
