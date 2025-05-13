package tech.intellispaces.reflections.framework.guide.n3;

import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.traverse.plan.TraverseExecutor;
import tech.intellispaces.reflections.framework.traverse.plan.TraversePlan;

/**
 * Three times parametrized automatic mover guide.
 *
 * <p>Automatic guide builds the traverse plan itself.
 *
 * @param <S> source object handle type.
 * @param <Q1> first qualifier handle type.
 * @param <Q2> second qualifier handle type.
 */
public class AutoMover3<S, Q1, Q2, Q3> implements AbstractMover3<S, Q1, Q2, Q3> {
  private final String cid;
  private final TraversePlan traversePlan;
  private final ReflectionForm targetForm;
  private final TraverseExecutor traverseExecutor;

  public AutoMover3(
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
  public S traverse(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3) throws TraverseException {
    return (S) traversePlan.execute(source, qualifier1, qualifier2, qualifier3, traverseExecutor);
  }
}
