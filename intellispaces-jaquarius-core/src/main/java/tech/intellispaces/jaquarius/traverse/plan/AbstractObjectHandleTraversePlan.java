package tech.intellispaces.jaquarius.traverse.plan;

import tech.intellispaces.jaquarius.object.ObjectFunctions;
import tech.intellispaces.entity.exception.UnexpectedExceptions;

import java.util.HashMap;
import java.util.Map;

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
  public String cid() {
    return cid;
  }

  @Override
  public ExecutionTraversePlan getExecutionPlan(Class<?> sourceClass) {
    if (lastSourceClass != null && lastSourceClass == sourceClass) {
      return lastExecutionPlan;
    }
    lastSourceClass = sourceClass;
    lastExecutionPlan = executionPlans.get(lastSourceClass);
    return lastExecutionPlan;
  }

  @Override
  public void addExecutionPlan(Class<?> sourceClass, ExecutionTraversePlan traversePlan) {
    if (!ObjectFunctions.isObjectHandleClass(sourceClass)) {
      throw UnexpectedExceptions.withMessage("Expected object handle class");
    }
    if (this.objectHandleClass != sourceClass && !this.objectHandleClass.isAssignableFrom(sourceClass)) {
      throw UnexpectedExceptions.withMessage("Expected class {0} or its subclasses",
          this.objectHandleClass.getCanonicalName());
    }
    executionPlans.put(sourceClass, traversePlan);
  }
}
