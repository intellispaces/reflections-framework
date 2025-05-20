package tech.intellispaces.reflections.framework.traverse.plan;

import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.system.TraverseExecutor;

public class MapOfMovingThruChannel1TraversePlanImpl extends AbstractTraverseThruChannelPlan
    implements MapOfMovingThruChannel1TraversePlan
{
  public MapOfMovingThruChannel1TraversePlanImpl(Class<?> reflectionClass, String cid) {
    super(reflectionClass, cid);
  }

  @Override
  public TraversePlanType type() {
    return TraversePlanTypes.MapOfMovingThruChannel1;
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
