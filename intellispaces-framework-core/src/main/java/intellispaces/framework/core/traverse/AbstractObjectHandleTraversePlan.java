package intellispaces.framework.core.traverse;

import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.framework.core.object.ObjectFunctions;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractObjectHandleTraversePlan implements ObjectHandleTraversePlan {
  private final Class<?> objectHandleClass;
  private final String tid;
  private final Map<Class<?>, ExecutionPlan> actualPlans = new HashMap<>();

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
  public ExecutionPlan getExecutionPlan(Class<?> objectHandleClass) {
    return actualPlans.get(objectHandleClass);
  }

  @Override
  public void addExecutionPlan(Class<?> objectHandleClass, ExecutionPlan traversePlan) {
    if (!ObjectFunctions.isObjectHandleClass(objectHandleClass)) {
      throw UnexpectedViolationException.withMessage("Expected object handle class");
    }
    if (this.objectHandleClass != objectHandleClass && !this.objectHandleClass.isAssignableFrom(objectHandleClass)) {
      throw UnexpectedViolationException.withMessage("Expected class {0} or its subclasses",
          this.objectHandleClass.getCanonicalName());
    }
    actualPlans.put(objectHandleClass, traversePlan);
  }
}
