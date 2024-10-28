package intellispaces.jaquarius.guide.n1;

import intellispaces.jaquarius.exception.TraverseException;
import intellispaces.jaquarius.guide.GuideForm;
import intellispaces.jaquarius.traverse.plan.TraverseExecutor;
import intellispaces.jaquarius.traverse.plan.TraversePlan;

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
  private final GuideForm guideForm;
  private final TraverseExecutor traverseExecutor;

  public AutoMover1(
      String cid, TraversePlan traversePlan, GuideForm guideForm, TraverseExecutor traverseExecutor
  ) {
    this.cid = cid;
    this.traversePlan = traversePlan;
    this.guideForm = guideForm;
    this.traverseExecutor = traverseExecutor;
  }

  @Override
  public String cid() {
    return cid;
  }

  @Override
  public GuideForm guideForm() {
    return guideForm;
  }

  @Override
  @SuppressWarnings("unchecked")
  public S traverse(S source, Q qualifier) throws TraverseException {
    return (S) traversePlan.execute(source, qualifier, traverseExecutor);
  }
}
