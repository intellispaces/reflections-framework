package intellispaces.framework.core.guide.n0;

import intellispaces.framework.core.exception.TraverseException;
import intellispaces.framework.core.traverse.plan.DeclarativePlan;
import intellispaces.framework.core.traverse.plan.TraverseExecutor;

/**
 * Not-parametrized automatic mapper of moving.
 *
 * <p>Automatic guide builds the traverse plan itself.
 *
 * @param <S> source object handle type.
 * @param <T> target object handle type.
 */
public class AutoMapperOfMoving0<S, T> implements AbstractMapperOfMoving0<S, T> {
  private final String cid;
  private final TraverseExecutor traverseExecutor;
  private final DeclarativePlan declarativeTaskPlan;

  public AutoMapperOfMoving0(String cid, DeclarativePlan declarativeTaskPlan, TraverseExecutor traverseExecutor) {
    this.cid = cid;
    this.declarativeTaskPlan = declarativeTaskPlan;
    this.traverseExecutor = traverseExecutor;
  }

  @Override
  public String cid() {
    return cid;
  }

  @Override
  @SuppressWarnings("unchecked")
  public T traverse(S source) throws TraverseException {
    return (T) declarativeTaskPlan.execute(source, traverseExecutor);
  }
}
