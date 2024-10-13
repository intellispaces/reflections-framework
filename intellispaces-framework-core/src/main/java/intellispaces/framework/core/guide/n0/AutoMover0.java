package intellispaces.framework.core.guide.n0;

import intellispaces.framework.core.exception.TraverseException;
import intellispaces.framework.core.guide.GuideForm;
import intellispaces.framework.core.traverse.plan.TraverseExecutor;
import intellispaces.framework.core.traverse.plan.TraversePlan;

/**
 * Not-parametrized automatic mover guide.
 *
 * <p>Automatic guide builds the traverse plan itself.
 *
 * @param <S> source object handle type.
 */
public class AutoMover0<S> implements AbstractMover0<S> {
  private final String cid;
  private final TraversePlan traversePlan;
  private final GuideForm guideForm;
  private final TraverseExecutor traverseExecutor;

  public AutoMover0(
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
  public S traverse(S source) throws TraverseException {
    return (S) traversePlan.execute(source, traverseExecutor);
  }
}
