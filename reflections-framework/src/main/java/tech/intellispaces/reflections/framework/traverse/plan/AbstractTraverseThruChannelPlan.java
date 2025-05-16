package tech.intellispaces.reflections.framework.traverse.plan;

import java.util.HashMap;
import java.util.Map;

import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.reflections.framework.reflection.ReflectionFunctions;

public abstract class AbstractTraverseThruChannelPlan implements TraverseThruChannelPlan {
  private final Class<?> reflectionClass;
  private final String cid;
  private final Map<Class<?>, ExecutionTraversePlan> executionPlans = new HashMap<>();
  private Class<?> lastSourceClass;
  private ExecutionTraversePlan lastExecutionPlan;

  public AbstractTraverseThruChannelPlan(Class<?> reflectionClass, String cid) {
    this.reflectionClass = reflectionClass;
    this.cid = cid;
  }

  @Override
  public Class<?> reflectionClass() {
    return reflectionClass;
  }

  @Override
  public String channelId() {
    return cid;
  }

  @Override
  public ExecutionTraversePlan cachedExecutionPlan(Class<?> sourceClass) {
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
  public void cacheExecutionPlan(Class<?> sourceClass, ExecutionTraversePlan traversePlan) {
    if (!ReflectionFunctions.isObjectHandleClass(sourceClass)) {
      throw UnexpectedExceptions.withMessage("Expected reflection class");
    }
    if (this.reflectionClass != sourceClass && !this.reflectionClass.isAssignableFrom(sourceClass)) {
      throw UnexpectedExceptions.withMessage("Expected class {0} or its subclasses",
          this.reflectionClass.getCanonicalName());
    }
    executionPlans.put(sourceClass, traversePlan);
  }
}
