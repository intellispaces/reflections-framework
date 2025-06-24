package tech.intellispaces.reflections.framework.guide.n1;

import tech.intellispaces.core.Rid;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.system.TraverseExecutor;
import tech.intellispaces.reflections.framework.task.plan.TraversePlan;

/**
 * One-parametrized automatic mapper of moving.
 *
 * <p>Automatic guide builds the traverse plan itself.
 *
 * @param <S> the source reflection type.
 * @param <T> the target reflection type.
 * @param <Q> the qualifier reflection type.
 */
public class AutoMapperOfMoving1<S, T, Q> implements AbstractMapperOfMoving1<S, T, Q> {
  private final Rid cid;
  private final TraversePlan traversePlan;
  private final Class<S> sourceClass;
  private final ReflectionForm targetForm;
  private final TraverseExecutor traverseExecutor;

  public AutoMapperOfMoving1(
      Rid cid,
      TraversePlan traversePlan,
      Class<S> sourceClass,
      ReflectionForm targetForm,
      TraverseExecutor traverseExecutor
  ) {
    this.cid = cid;
    this.traversePlan = traversePlan;
    this.sourceClass = sourceClass;
    this.targetForm = targetForm;
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
  public T traverse(S source, Q qualifier) throws TraverseException {
    return (T) traversePlan.execute(source, qualifier, traverseExecutor);
  }
}
