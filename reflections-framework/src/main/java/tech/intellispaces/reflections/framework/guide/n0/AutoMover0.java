package tech.intellispaces.reflections.framework.guide.n0;

import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.system.TraverseExecutor;
import tech.intellispaces.reflections.framework.traverse.plan.TraversePlan;

/**
 * Not-parametrized automatic mover guide.
 *
 * <p>Automatic guide builds the traverse plan itself.
 *
 * @param <S> the source reflection type.
 */
public class AutoMover0<S> implements AbstractMover0<S> {
  private final String cid;
  private final TraversePlan traversePlan;
  private final ReflectionForm targetForm;
  private final TraverseExecutor traverseExecutor;

  public AutoMover0(
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
  public S traverse(S source) throws TraverseException {
    return (S) traversePlan.execute(source, traverseExecutor);
  }
}
