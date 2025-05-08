package tech.intellispaces.reflections.traverse.plan;

import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.reflections.exception.TraverseException;

public class MapObjectHandleThruChannel3PlanImpl extends AbstractObjectHandleTraversePlan
    implements MapObjectHandleThruChannel3Plan
{
  public MapObjectHandleThruChannel3PlanImpl(Class<?> objectHandleClass, String cid) {
    super(objectHandleClass, cid);
  }

  @Override
  public TraversePlanType type() {
    return TraversePlanTypes.MapObjectHandleThruChannel3;
  }

  @Override
  public Object execute(Object source, TraverseExecutor executor) throws TraverseException {
    throw UnexpectedExceptions.withMessage("Expected traverse with three qualifiers");
  }

  @Override
  public int executeReturnInt(Object source, TraverseExecutor executor) throws TraverseException {
    throw UnexpectedExceptions.withMessage("Expected traverse with three qualifiers");
  }

  @Override
  public double executeReturnDouble(Object source, TraverseExecutor executor) throws TraverseException {
    throw UnexpectedExceptions.withMessage("Expected traverse with three qualifiers");
  }

  @Override
  public Object execute(Object source, Object qualifier, TraverseExecutor executor) throws TraverseException {
    throw UnexpectedExceptions.withMessage("Expected traverse with three qualifiers");
  }

  @Override
  public Object execute(
    Object source, Object qualifier1, Object qualifier2, TraverseExecutor executor
  ) throws TraverseException {
    throw UnexpectedExceptions.withMessage("Expected traverse with three qualifiers");
  }

  @Override
  public Object execute(
      Object source, Object qualifier1, Object qualifier2, Object qualifier3, TraverseExecutor executor
  ) throws TraverseException {
    return executor.execute(this, source, qualifier1, qualifier2, qualifier3);
  }

  @Override
  public Object execute(
      Object source, Object qualifier1, Object qualifier2, Object qualifier3, Object qualifier4, TraverseExecutor executor
  ) throws TraverseException {
    throw UnexpectedExceptions.withMessage("Expected traverse with three qualifiers");
  }
}
