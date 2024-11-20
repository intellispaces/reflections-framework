package intellispaces.jaquarius.traverse.plan;

import intellispaces.jaquarius.exception.TraverseException;
import tech.intellispaces.entity.exception.UnexpectedExceptions;

public class MapObjectHandleThruChannel1PlanImpl extends AbstractObjectHandleTraversePlan
    implements MapObjectHandleThruChannel1Plan
{
  public MapObjectHandleThruChannel1PlanImpl(Class<?> objectHandleClass, String cid) {
    super(objectHandleClass, cid);
  }

  @Override
  public TraversePlanType type() {
    return TraversePlanTypes.MapObjectHandleThruChannel1;
  }

  @Override
  public Object execute(Object source, TraverseExecutor executor) throws TraverseException {
    throw UnexpectedExceptions.withMessage("Expected traverse with one qualifier");
  }

  @Override
  public int executeReturnInt(Object source, TraverseExecutor executor) throws TraverseException {
    throw UnexpectedExceptions.withMessage("Expected traverse with one qualifier");
  }

  @Override
  public double executeReturnDouble(Object source, TraverseExecutor executor) throws TraverseException {
    throw UnexpectedExceptions.withMessage("Expected traverse with one qualifier");
  }

  @Override
  public Object execute(Object source, Object qualifier, TraverseExecutor executor) throws TraverseException {
    return executor.execute(this, source, qualifier);
  }

  @Override
  public Object execute(
      Object source, Object qualifier1, Object qualifier2, TraverseExecutor executor
  ) throws TraverseException {
    throw UnexpectedExceptions.withMessage("Expected traverse with one qualifier");
  }

  @Override
  public Object execute(
    Object source, Object qualifier1, Object qualifier2, Object qualifier3, TraverseExecutor executor
  ) throws TraverseException {
    throw UnexpectedExceptions.withMessage("Expected traverse with one qualifier");
  }

  @Override
  public Object execute(
      Object source, Object qualifier1, Object qualifier2, Object qualifier3, Object qualifier4, TraverseExecutor executor
  ) throws TraverseException {
    throw UnexpectedExceptions.withMessage("Expected traverse with one qualifier");
  }
}
