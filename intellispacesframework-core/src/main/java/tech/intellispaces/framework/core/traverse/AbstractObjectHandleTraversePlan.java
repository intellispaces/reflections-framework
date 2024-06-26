package tech.intellispaces.framework.core.traverse;

import tech.intellispaces.framework.commons.exception.UnexpectedViolationException;
import tech.intellispaces.framework.core.object.ObjectFunctions;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractObjectHandleTraversePlan implements ObjectHandleTraversePlan {
  private final Class<?> objectHandleClass;
  private final String tid;
  private final Map<Class<?>, ActualPlan> actualPlans = new HashMap<>();

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
  public ActualPlan getActualPlan(Class<?> objectHandleClass) {
    return actualPlans.get(objectHandleClass);
  }

  @Override
  public void addActualPlan(Class<?> objectHandleClass, ActualPlan traversePlan) {
    if (!ObjectFunctions.isObjectHandleClass(objectHandleClass)) {
      throw UnexpectedViolationException.withMessage("Expected object handle class");
    }
    if (this.objectHandleClass != objectHandleClass && !this.objectHandleClass.isAssignableFrom(objectHandleClass)) {
      throw UnexpectedViolationException.withMessage("Expected class {} or its subclasses",
          this.objectHandleClass.getCanonicalName());
    }
    actualPlans.put(objectHandleClass, traversePlan);
  }
}
