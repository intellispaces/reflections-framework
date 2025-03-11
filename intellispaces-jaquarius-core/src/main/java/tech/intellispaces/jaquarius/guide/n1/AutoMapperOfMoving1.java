package tech.intellispaces.jaquarius.guide.n1;

import tech.intellispaces.jaquarius.exception.TraverseException;
import tech.intellispaces.jaquarius.object.reference.ObjectForm;
import tech.intellispaces.jaquarius.traverse.plan.TraverseExecutor;
import tech.intellispaces.jaquarius.traverse.plan.TraversePlan;

/**
 * One-parametrized automatic mapper of moving.
 *
 * <p>Automatic guide builds the traverse plan itself.
 *
 * @param <S> source object handle type.
 * @param <T> target object handle type.
 * @param <Q> qualifier handle type.
 */
public class AutoMapperOfMoving1<S, T, Q> implements AbstractMapperOfMoving1<S, T, Q> {
  private final String cid;
  private final TraversePlan traversePlan;
  private final ObjectForm targetForm;
  private final TraverseExecutor traverseExecutor;

  public AutoMapperOfMoving1(
      String cid, TraversePlan traversePlan, ObjectForm targetForm, TraverseExecutor traverseExecutor
  ) {
    this.cid = cid;
    this.traversePlan = traversePlan;
    this.targetForm = targetForm;
    this.traverseExecutor = traverseExecutor;
  }

  @Override
  public String cid() {
    return cid;
  }

  @Override
  public ObjectForm targetForm() {
    return targetForm;
  }

  @Override
  @SuppressWarnings("unchecked")
  public T traverse(S source, Q qualifier) throws TraverseException {
    return (T) traversePlan.execute(source, qualifier, traverseExecutor);
  }
}
