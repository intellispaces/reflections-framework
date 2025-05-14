package tech.intellispaces.reflections.framework.traverse.plan;

import java.util.HashMap;
import java.util.Map;

import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.reflections.framework.reflection.ReflectionFunctions;

public abstract class AbstractObjectHandleTraversePlan implements ObjectHandleTraversePlan {
  private final Class<?> objectHandleClass;
  private final String cid;
  private final Map<Class<?>, ExecutionTraversePlan> executionPlans = new HashMap<>();
  private Class<?> lastSourceClass;
  private ExecutionTraversePlan lastExecutionPlan;

  public AbstractObjectHandleTraversePlan(Class<?> objectHandleClass, String cid) {
    this.objectHandleClass = objectHandleClass;
    this.cid = cid;
  }

  @Override
  public Class<?> objectHandleClass() {
    return objectHandleClass;
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
    if (this.objectHandleClass != sourceClass && !this.objectHandleClass.isAssignableFrom(sourceClass)) {
      throw UnexpectedExceptions.withMessage("Expected class {0} or its subclasses",
          this.objectHandleClass.getCanonicalName());
    }
    executionPlans.put(sourceClass, traversePlan);
  }
}
