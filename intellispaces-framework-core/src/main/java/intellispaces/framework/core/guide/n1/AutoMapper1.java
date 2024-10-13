package intellispaces.framework.core.guide.n1;

import intellispaces.framework.core.exception.TraverseException;
import intellispaces.framework.core.guide.GuideForm;
import intellispaces.framework.core.traverse.plan.TraverseExecutor;
import intellispaces.framework.core.traverse.plan.TraversePlan;

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
  private final GuideForm guideForm;
  private final TraverseExecutor traverseExecutor;

  public AutoMapper1(
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
  public T traverse(S source, Q qualifier) throws TraverseException {
    return (T) traversePlan.execute(source, qualifier, traverseExecutor);
  }
}
