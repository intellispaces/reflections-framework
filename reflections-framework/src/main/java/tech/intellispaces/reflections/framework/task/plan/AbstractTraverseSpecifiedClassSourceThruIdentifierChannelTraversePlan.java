package tech.intellispaces.reflections.framework.task.plan;

import java.util.HashMap;
import java.util.Map;

import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.core.Rid;
import tech.intellispaces.reflections.framework.reflection.ReflectionFunctions;

public abstract class AbstractTraverseSpecifiedClassSourceThruIdentifierChannelTraversePlan
  implements TraverseSourceSpecifiedClassThruIdentifierChannelTraversePlan
{
  private final Class<?> sourceClass;
  private final Rid cid;
  private final Map<Class<?>, ExecutionTraversePlan> executionPlans = new HashMap<>();
  private Class<?> lastSourceClass;
  private ExecutionTraversePlan lastExecutionPlan;

  public AbstractTraverseSpecifiedClassSourceThruIdentifierChannelTraversePlan(Class<?> sourceClass, Rid cid) {
    this.sourceClass = sourceClass;
    this.cid = cid;
  }

  @Override
  public Class<?> sourceClass() {
    return sourceClass;
  }

  @Override
  public Rid channelId() {
    return cid;
  }

  @Override
  public ExecutionTraversePlan executionPlan(Class<?> sourceClass) {
    if (lastSourceClass != null && lastSourceClass == sourceClass) {
      return lastExecutionPlan;
    }
    lastExecutionPlan = executionPlans.get(sourceClass);
    if (lastExecutionPlan != null) {
      lastSourceClass = sourceClass;
    }
    return lastExecutionPlan;
  }

  @Override
  public void addExecutionPlan(Class<?> sourceClass, ExecutionTraversePlan plan) {
    if (!ReflectionFunctions.isReflectionClass(sourceClass)) {
      throw UnexpectedExceptions.withMessage("Expected reflection class");
    }
    if (this.sourceClass != sourceClass && !this.sourceClass.isAssignableFrom(sourceClass)) {
      throw UnexpectedExceptions.withMessage("Expected class {0} or its subclasses",
          this.sourceClass.getCanonicalName());
    }
    executionPlans.put(sourceClass, plan);
  }
}
