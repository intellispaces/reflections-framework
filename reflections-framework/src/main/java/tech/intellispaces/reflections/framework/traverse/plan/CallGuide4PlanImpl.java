package tech.intellispaces.reflections.framework.traverse.plan;

import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.guide.n4.Guide4;
import tech.intellispaces.reflections.framework.system.TraverseExecutor;

public class CallGuide4PlanImpl extends AbstractTraversePlan implements CallGuide4Plan {
  private final Guide4<Object, Object, Object, Object, Object, Object> guide;

  @SuppressWarnings("unchecked")
  public CallGuide4PlanImpl(Guide4<?, ?, ?, ?, ?, ?> guide) {
    this.guide = (Guide4<Object, Object, Object, Object, Object, Object>) guide;
  }

  @Override
  public TraversePlanType type() {
    return TraversePlanTypes.CallLocalGuide4;
  }

  @Override
  public Guide4<?, ?, ?, ?, ?, ?> guide() {
    return guide;
  }

  @Override
  public Object execute(
      Object source, Object qualifier1, Object qualifier2, Object qualifier3, Object qualifier4, TraverseExecutor executor
  ) throws TraverseException {
    return executor.execute(this, source, qualifier1, qualifier2, qualifier3, qualifier4);
  }
}
