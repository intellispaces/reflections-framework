package tech.intellispaces.jaquarius.guide.n2;

import tech.intellispaces.jaquarius.exception.TraverseException;
import tech.intellispaces.jaquarius.object.reference.ObjectForm;
import tech.intellispaces.jaquarius.traverse.plan.TraverseExecutor;
import tech.intellispaces.jaquarius.traverse.plan.TraversePlan;

/**
 * Two-parametrized automatic mover guide.
 *
 * <p>Automatic guide builds the traverse plan itself.
 *
 * @param <S> source object handle type.
 * @param <Q1> first qualifier handle type.
 * @param <Q2> second qualifier handle type.
 */
public class AutoMover2<S, Q1, Q2> implements AbstractMover2<S, Q1, Q2> {
  private final String cid;
  private final TraversePlan traversePlan;
  private final ObjectForm targetForm;
  private final TraverseExecutor traverseExecutor;

  public AutoMover2(
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
  public S traverse(S source, Q1 qualifier1, Q2 qualifier2) throws TraverseException {
    return (S) traversePlan.execute(source, qualifier1, qualifier2, traverseExecutor);
  }
}
