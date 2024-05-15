package tech.intellispacesframework.core.traverse;

import tech.intellispacesframework.commons.exception.UnexpectedViolationException;
import tech.intellispacesframework.core.exception.TraverseException;
import tech.intellispacesframework.core.object.ObjectFunctions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MoveObjectHandleThruTransition1TraversePlanDefault implements MoveObjectHandleThruTransition1TraversePlan {
  private final Class<?> objectHandleClass;
  private final String tid;
  private final Map<Class<?>, EffectiveTraversePlan> effectiveTaskPlans = new HashMap<>();

  public MoveObjectHandleThruTransition1TraversePlanDefault(Class<?> objectHandleClass, String tid) {
    this.objectHandleClass = objectHandleClass;
    this.tid = tid;
  }

  @Override
  public TraversePlanType type() {
    return TraversePlanTypes.MoveObjectHandleThruTransition1;
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
  public Map<Class<?>, EffectiveTraversePlan> effectiveTaskPlans() {
    return Collections.unmodifiableMap(effectiveTaskPlans);
  }

  @Override
  public void addEffectiveTaskPlan(Class<?> objectHandleClass, EffectiveTraversePlan traversePlan) {
    if (this.objectHandleClass != objectHandleClass && !this.objectHandleClass.isAssignableFrom(objectHandleClass)) {
      throw UnexpectedViolationException.withMessage("Expected class {} or its subclasses", this.objectHandleClass.getCanonicalName());
    }
    if (!ObjectFunctions.isObjectHandleClass(objectHandleClass)) {
      throw UnexpectedViolationException.withMessage("Expected object handle class");
    }
    effectiveTaskPlans.put(objectHandleClass, traversePlan);
  }

  @Override
  public Object execute(Object source, TraverseExecutor traverseExecutor) {
    throw UnexpectedViolationException.withMessage("Expected traverse with one qualifier");
  }

  @Override
  public Object execute(Object source, Object qualifier, TraverseExecutor traverseExecutor) throws TraverseException {
    return traverseExecutor.execute(this, source, qualifier);
  }
}
