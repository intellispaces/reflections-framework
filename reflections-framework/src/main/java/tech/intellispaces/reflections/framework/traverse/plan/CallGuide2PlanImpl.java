package tech.intellispaces.reflections.framework.traverse.plan;

import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.guide.n2.Guide2;
import tech.intellispaces.reflections.framework.system.TraverseExecutor;

public class CallGuide2PlanImpl extends AbstractTraversePlan implements CallGuide2Plan {
  private final Guide2<Object, Object, Object, Object> guide;

  @SuppressWarnings("unchecked")
  public CallGuide2PlanImpl(Guide2<?, ?, ?, ?> guide) {
    this.guide = (Guide2<Object, Object, Object, Object>) guide;
  }

  @Override
  public TraversePlanType type() {
    return TraversePlanTypes.CallLocalGuide2;
  }

  @Override
  public Guide2<?, ?, ?, ?> guide() {
    return guide;
  }

  @Override
  public Object execute(
      Object source, Object qualifier1, Object qualifier2, TraverseExecutor executor
  ) throws TraverseException {
    return executor.execute(this, source, qualifier1, qualifier2);
  }
}
