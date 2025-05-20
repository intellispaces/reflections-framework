package tech.intellispaces.reflections.framework.traverse.plan;

import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.system.TraverseExecutor;

public class MapOfMovingThruChannel3TraversePlanImpl extends AbstractTraverseThruChannelPlan
    implements MapOfMovingThruChannel3TraversePlan
{
  public MapOfMovingThruChannel3TraversePlanImpl(Class<?> reflectionClass, String cid) {
    super(reflectionClass, cid);
  }

  @Override
  public TraversePlanType type() {
    return TraversePlanTypes.MapOfMovingThruChannel3;
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
