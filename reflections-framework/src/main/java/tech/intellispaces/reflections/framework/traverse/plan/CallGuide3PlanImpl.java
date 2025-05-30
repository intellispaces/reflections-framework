package tech.intellispaces.reflections.framework.traverse.plan;

import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.guide.n3.Guide3;
import tech.intellispaces.reflections.framework.system.TraverseExecutor;

public class CallGuide3PlanImpl extends AbstractTraversePlan implements CallGuide3Plan {
  private final Guide3<Object, Object, Object, Object, Object> guide;

  @SuppressWarnings("unchecked")
  public CallGuide3PlanImpl(Guide3<?, ?, ?, ?, ?> guide) {
    this.guide = (Guide3<Object, Object, Object, Object, Object>) guide;
  }

  @Override
  public TraversePlanType type() {
    return TraversePlanTypes.CallLocalGuide3;
  }

  @Override
  public Guide3<?, ?, ?, ?, ?> guide() {
    return guide;
  }

  @Override
  public Object execute(
      Object source, Object qualifier1, Object qualifier2, Object qualifier3, TraverseExecutor executor
  ) throws TraverseException {
    return executor.execute(this, source, qualifier1, qualifier2, qualifier3);
  }
}
