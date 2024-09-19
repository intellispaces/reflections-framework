package intellispaces.framework.core.traverse;

import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.framework.core.object.ObjectFunctions;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractObjectHandleTraversePlan implements ObjectHandleTraversePlan {
  private final Class<?> objectHandleClass;
  private final String tid;
  private final Map<Class<?>, ExecutionPlan> actualPlans = new HashMap<>();
  private Class<?> lastSourceClass;
  private ExecutionPlan lastExecutionPlan;

  public AbstractObjectHandleTraversePlan(Class<?> objectHandleClass, String tid) {
    this.objectHandleClass = objectHandleClass;
    this.tid = tid;
  }

  @Override
  public Class<?> objectHandleClass() {
    return objectHandleClass;
  }

  @Override
  public String tid() {
    return tid;
  }

  @Override
  public ExecutionPlan getExecutionPlan(Class<?> sourceClass) {
    if (lastSourceClass != null && lastSourceClass == sourceClass) {
      return lastExecutionPlan;
    }
    lastSourceClass = sourceClass;
    lastExecutionPlan = actualPlans.get(lastSourceClass);
    return lastExecutionPlan;
  }

  @Override
  public void addExecutionPlan(Class<?> sourceClass, ExecutionPlan traversePlan) {
    if (!ObjectFunctions.isObjectHandleClass(sourceClass)) {
      throw UnexpectedViolationException.withMessage("Expected object handle class");
    }
    if (this.objectHandleClass != sourceClass && !this.objectHandleClass.isAssignableFrom(sourceClass)) {
      throw UnexpectedViolationException.withMessage("Expected class {0} or its subclasses",
          this.objectHandleClass.getCanonicalName());
    }
    actualPlans.put(sourceClass, traversePlan);
  }
}
