package tech.intellispaces.reflections.framework.guide.n0;

import tech.intellispaces.core.Rid;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.system.TraverseExecutor;
import tech.intellispaces.reflections.framework.traverse.plan.TraversePlan;

/**
 * Not-parametrized automatic mapper guide.
 *
 * <p>Automatic guide builds the traverse plan itself.
 *
 * @param <S> the source reflection type.
 * @param <T> the target reflection type.
 */
public class AutoMapper0<S, T> implements AbstractMapper0<S, T> {
  private final Rid cid;
  private final TraversePlan traversePlan;
  private final Class<S> sourceClass;
  private final ReflectionForm targetForm;
  private final TraverseExecutor traverseExecutor;

  public AutoMapper0(
      Rid cid,
      TraversePlan traversePlan,
      Class<S> sourceClass,
      ReflectionForm targetForm,
      TraverseExecutor traverseExecutor
  ) {
    this.cid = cid;
    this.traversePlan = traversePlan;
    this.targetForm = targetForm;
    this.sourceClass = sourceClass;
    this.traverseExecutor = traverseExecutor;
  }

  @Override
  public Rid channelId() {
    return cid;
  }

  @Override
  public Class<S> sourceClass() {
    return sourceClass;
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
