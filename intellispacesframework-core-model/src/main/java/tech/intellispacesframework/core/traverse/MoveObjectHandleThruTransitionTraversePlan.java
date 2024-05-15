package tech.intellispacesframework.core.traverse;

import java.util.Map;

/**
 * Declarative traverse plan to move object handle specific class through transition.
 */
public interface MoveObjectHandleThruTransitionTraversePlan extends DeclarativeTraversePlan {

  /**
   * Source object handle class.
   */
  Class<?> objectHandleClass();

  /**
   * Transition ID.
   */
  String tid();

  Map<Class<?>, EffectiveTraversePlan> effectiveTaskPlans();

  void addEffectiveTaskPlan(Class<?> objectHandleClass, EffectiveTraversePlan traversePlan);
}
