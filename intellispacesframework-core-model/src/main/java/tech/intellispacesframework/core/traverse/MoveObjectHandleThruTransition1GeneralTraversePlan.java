package tech.intellispacesframework.core.traverse;

import java.util.Map;

/**
 * Declarative traverse plan to move object handle specific class through one-parametrized transition.
 */
public interface MoveObjectHandleThruTransition1GeneralTraversePlan extends DeclarativeTraversePlan {

  /**
   * Source object handle class.
   */
  Class<?> objectHandleClass();

  /**
   * Transition ID.
   */
  String tid();

  Map<Class<?>, EffectiveTraversePlan> effectiveTaskPlans();

  void setEffectiveTaskPlan(Class<?> objectHandleClass, EffectiveTraversePlan traversePlan);
}
