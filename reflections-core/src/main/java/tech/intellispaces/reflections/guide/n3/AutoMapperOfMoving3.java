package tech.intellispaces.reflections.guide.n3;

import tech.intellispaces.reflections.exception.TraverseException;
import tech.intellispaces.reflections.object.reference.ObjectReferenceForm;
import tech.intellispaces.reflections.traverse.plan.TraverseExecutor;
import tech.intellispaces.reflections.traverse.plan.TraversePlan;

/**
 * Three times parametrized automatic mapper of moving.
 *
 * <p>Automatic guide builds the traverse plan itself.
 *
 * @param <S> source object handle type.
 * @param <T> target object handle type.
 * @param <Q1> first qualifier handle type.
 * @param <Q2> second qualifier handle type.
 * @param <Q3> third qualifier handle type.
 */
public class AutoMapperOfMoving3<S, T, Q1, Q2,  Q3> implements AbstractMapperOfMoving3<S, T, Q1, Q2, Q3> {
  private final String cid;
  private final TraversePlan traversePlan;
  private final ObjectReferenceForm targetForm;
  private final TraverseExecutor traverseExecutor;

  public AutoMapperOfMoving3(
      String cid, TraversePlan traversePlan, ObjectReferenceForm targetForm, TraverseExecutor traverseExecutor
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
  public ObjectReferenceForm targetForm() {
    return targetForm;
  }

  @Override
  @SuppressWarnings("unchecked")
  public T traverse(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3) throws TraverseException {
    return (T) traversePlan.execute(source, qualifier1, qualifier2, qualifier3, traverseExecutor);
  }
}
