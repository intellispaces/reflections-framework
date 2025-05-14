package tech.intellispaces.reflections.framework.guide.n0;

import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.traverse.plan.TraverseExecutor;
import tech.intellispaces.reflections.framework.traverse.plan.TraversePlan;

/**
 * Not-parametrized automatic mapper of moving.
 *
 * <p>Automatic guide builds the traverse plan itself.
 *
 * @param <S> the source reflection type.
 * @param <T> the target reflection type.
 */
public class AutoMapperOfMoving0<S, T> implements AbstractMapperOfMoving0<S, T> {
  private final String cid;
  private final TraversePlan traversePlan;
  private final ReflectionForm targetForm;
  private final TraverseExecutor traverseExecutor;

  public AutoMapperOfMoving0(
          String cid, TraversePlan traversePlan, ReflectionForm targetForm, TraverseExecutor traverseExecutor
  ) {
    this.cid = cid;
    this.traversePlan = traversePlan;
    this.targetForm = targetForm;
    this.traverseExecutor = traverseExecutor;
  }

  @Override
  public String channelId() {
    return cid;
  }

  @Override
  public ReflectionForm targetForm() {
    return targetForm;
  }

  @Override
  @SuppressWarnings("unchecked")
  public T traverse(S source) throws TraverseException {
    return (T) traversePlan.execute(source, traverseExecutor);
  }

  @Override
  public int traverseToInt(S source) throws TraverseException {
    return traversePlan.executeReturnInt(source, traverseExecutor);
  }

  @Override
  public double traverseToDouble(S source) throws TraverseException {
    return traversePlan.executeReturnDouble(source, traverseExecutor);
  }
}
