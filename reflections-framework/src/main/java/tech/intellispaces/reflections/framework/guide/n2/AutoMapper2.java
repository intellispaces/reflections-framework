package tech.intellispaces.reflections.framework.guide.n2;

import tech.intellispaces.core.Rid;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.system.TraverseExecutor;
import tech.intellispaces.reflections.framework.task.plan.TraversePlan;

/**
 * Two-parametrized automatic mapper guide.
 *
 * <p>Automatic guide builds the traverse plan itself.
 *
 * @param <S> the source reflection type.
 * @param <T> the target reflection type.
 * @param <Q1> the first qualifier reflection type.
 * @param <Q2> the second qualifier reflection type.
 */
public class AutoMapper2<S, T, Q1, Q2> implements AbstractMapper2<S, T, Q1, Q2> {
  private final Rid cid;
  private final TraversePlan traversePlan;
  private final Class<S> sourceClass;
  private final ReflectionForm targetForm;
  private final TraverseExecutor traverseExecutor;

  public AutoMapper2(
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
  public T traverse(S source, Q1 qualifier1, Q2 qualifier2) throws TraverseException {
    return (T) traversePlan.execute(source, qualifier1, qualifier2, traverseExecutor);
  }
}
