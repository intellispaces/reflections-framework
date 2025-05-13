package tech.intellispaces.reflections.framework.guide.n2;

import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.traverse.plan.TraverseExecutor;
import tech.intellispaces.reflections.framework.traverse.plan.TraversePlan;

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
  private final String cid;
  private final TraversePlan traversePlan;
  private final ReflectionForm targetForm;
  private final TraverseExecutor traverseExecutor;

  public AutoMapper2(
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
  public T traverse(S source, Q1 qualifier1, Q2 qualifier2) throws TraverseException {
    return (T) traversePlan.execute(source, qualifier1, qualifier2, traverseExecutor);
  }
}
