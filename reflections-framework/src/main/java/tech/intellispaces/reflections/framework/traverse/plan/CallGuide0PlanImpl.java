package tech.intellispaces.reflections.framework.traverse.plan;

import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.guide.n0.Guide0;
import tech.intellispaces.reflections.framework.system.TraverseExecutor;

public class CallGuide0PlanImpl extends AbstractTraversePlan implements CallGuide0Plan {
  private final Guide0<Object, Object> guide;

  @SuppressWarnings("unchecked")
  public CallGuide0PlanImpl(Guide0<?, ?> guide) {
    this.guide = (Guide0<Object, Object>) guide;
  }

  @Override
  public TraversePlanType type() {
    return TraversePlanTypes.CallLocalGuide0;
  }

  @Override
  public Guide0<?, ?> guide() {
    return guide;
  }

  @Override
  public Object execute(Object source, TraverseExecutor executor) throws TraverseException {
    return executor.execute(this, source);
  }

  @Override
  public int executeReturnInt(Object source, TraverseExecutor executor) throws TraverseException {
    return executor.executeReturnInt(this, source);
  }

  @Override
  public double executeReturnDouble(Object source, TraverseExecutor executor) throws TraverseException {
    return executor.executeReturnInt(this, source);
  }
}
