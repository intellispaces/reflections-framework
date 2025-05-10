package tech.intellispaces.reflectionsframework.guide.n1;

import tech.intellispaces.reflectionsframework.exception.TraverseException;
import tech.intellispaces.reflectionsframework.object.reference.ObjectReferenceForm;
import tech.intellispaces.reflectionsframework.traverse.plan.TraverseExecutor;
import tech.intellispaces.reflectionsframework.traverse.plan.TraversePlan;

/**
 * One-parametrized automatic mover guide.
 *
 * <p>Automatic guide builds the traverse plan itself.
 *
 * @param <S> source object handle type.
 * @param <Q> qualifier handle type.
 */
public class AutoMover1<S, Q> implements AbstractMover1<S, Q> {
  private final String cid;
  private final TraversePlan traversePlan;
  private final ObjectReferenceForm targetForm;
  private final TraverseExecutor traverseExecutor;

  public AutoMover1(
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
  public S traverse(S source, Q qualifier) throws TraverseException {
    return (S) traversePlan.execute(source, qualifier, traverseExecutor);
  }
}
