package tech.intellispacesframework.core.system;

import tech.intellispacesframework.commons.exception.UnexpectedViolationException;
import tech.intellispacesframework.core.exception.TraverseException;
import tech.intellispacesframework.core.guide.n1.Guide1;
import tech.intellispacesframework.core.object.ObjectFunctions;
import tech.intellispacesframework.core.traverse.CallGuide1TraversePlan;
import tech.intellispacesframework.core.traverse.EffectiveTraversePlan;
import tech.intellispacesframework.core.traverse.MoveObjectHandleThruTransition1GeneralTraversePlan;
import tech.intellispacesframework.core.traverse.TraverseAnalyzer;
import tech.intellispacesframework.core.traverse.TraverseExecutor;
import tech.intellispacesframework.core.traverse.TraversePlanTypes;

import java.util.Map;

class TraverseExecutorDefault implements TraverseExecutor {
  private final TraverseAnalyzer traverseAnalyzer;

  TraverseExecutorDefault(TraverseAnalyzer traverseAnalyzer) {
    this.traverseAnalyzer = traverseAnalyzer;
  }

  @Override
  @SuppressWarnings("unchecked")
  public Object execute(
      CallGuide1TraversePlan traversePlan, Object source, Object qualifier
  ) throws TraverseException {
    var guide = (Guide1<Object, Object, Object>) traversePlan.guide();
    return guide.traverse(source, qualifier);
  }

  @Override
  public Object execute(
      MoveObjectHandleThruTransition1GeneralTraversePlan traversePlan, Object source, Object qualifier
  ) throws TraverseException {
    if (source.getClass() != traversePlan.objectHandleClass() && !traversePlan.objectHandleClass().isAssignableFrom(source.getClass())) {
      throw UnexpectedViolationException.withMessage("Traverse plan of type {} expected object handle of class {} or it subclass",
          traversePlan.objectHandleClass());
    }
    if (!ObjectFunctions.isObjectHandleClass(source.getClass())) {
      throw UnexpectedViolationException.withMessage("Traverse plan of type {} expected object handle to input",
          TraversePlanTypes.MoveObjectHandleThruTransition1);
    }

    Map<Class<?>, EffectiveTraversePlan> effectiveTraversePlans = traversePlan.effectiveTaskPlans();
    EffectiveTraversePlan effectiveTraversePlan = effectiveTraversePlans.get(traversePlan.objectHandleClass());
    if (effectiveTraversePlan == null) {
      effectiveTraversePlan = effectiveTraversePlans.get(source.getClass());
    }
    if (effectiveTraversePlan == null) {
      effectiveTraversePlan = traverseAnalyzer.buildEffectiveTaskPlanFor(traversePlan, traversePlan.objectHandleClass()).orElse(null);
      if (effectiveTraversePlan != null) {
        traversePlan.setEffectiveTaskPlan(traversePlan.objectHandleClass(), effectiveTraversePlan);
      } else {
        effectiveTraversePlan = traverseAnalyzer.buildEffectiveTaskPlanFor(traversePlan, source.getClass()).orElse(null);
        if (effectiveTraversePlan != null) {
          traversePlan.setEffectiveTaskPlan(source.getClass(), effectiveTraversePlan);
        }
      }
    }
    if (effectiveTraversePlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan");
    }
    return effectiveTraversePlan.execute(source, qualifier, this);
  }
}
