package tech.intellispaces.reflections.guide.n1;

import tech.intellispaces.reflections.exception.TraverseException;
import tech.intellispaces.reflections.object.reference.ObjectReferenceForm;
import tech.intellispaces.reflections.traverse.plan.TraverseExecutor;
import tech.intellispaces.reflections.traverse.plan.TraversePlan;

/**
 * One-parametrized automatic mapper guide.
 *
 * <p>Automatic guide builds the traverse plan itself.
 *
 * @param <S> source object handle type.
 * @param <T> target object handle type.
 * @param <Q> qualifier handle type.
 */
public class AutoMapper1<S, T, Q> implements AbstractMapper1<S, T, Q> {
  private final String cid;
  private final TraversePlan traversePlan;
  private final ObjectReferenceForm targetForm;
  private final TraverseExecutor traverseExecutor;

  public AutoMapper1(
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
  public T traverse(S source, Q qualifier) throws TraverseException {
    return (T) traversePlan.execute(source, qualifier, traverseExecutor);
  }
}
