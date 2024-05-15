package tech.intellispacesframework.core.system;

import tech.intellispacesframework.commons.exception.UnexpectedViolationException;
import tech.intellispacesframework.core.exception.TraverseException;
import tech.intellispacesframework.core.guide.n0.Guide0;
import tech.intellispacesframework.core.guide.n1.Guide1;
import tech.intellispacesframework.core.object.ObjectFunctions;
import tech.intellispacesframework.core.traverse.CallGuide0TraversePlan;
import tech.intellispacesframework.core.traverse.CallGuide1TraversePlan;
import tech.intellispacesframework.core.traverse.EffectiveTraversePlan;
import tech.intellispacesframework.core.traverse.MoveObjectHandleThruTransition0TraversePlan;
import tech.intellispacesframework.core.traverse.MoveObjectHandleThruTransition1TraversePlan;
import tech.intellispacesframework.core.traverse.MoveObjectHandleThruTransitionTraversePlan;
import tech.intellispacesframework.core.traverse.TraverseAnalyzer;
import tech.intellispacesframework.core.traverse.TraverseExecutor;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

class TraverseExecutorDefault implements TraverseExecutor {
  private final TraverseAnalyzer traverseAnalyzer;

  TraverseExecutorDefault(TraverseAnalyzer traverseAnalyzer) {
    this.traverseAnalyzer = traverseAnalyzer;
  }

  @Override
  @SuppressWarnings("unchecked")
  public Object execute(
      CallGuide0TraversePlan traversePlan, Object source
  ) throws TraverseException {
    var guide = (Guide0<Object, Object>) traversePlan.guide();
    return guide.traverse(source);
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
      MoveObjectHandleThruTransition0TraversePlan traversePlan, Object source
  ) throws TraverseException {
    EffectiveTraversePlan effectiveTraversePlan = getEffectiveTraversePlanFor(
        traversePlan,
        source,
        aClass -> traverseAnalyzer.buildEffectiveTaskPlanFor(traversePlan, aClass)
    );
    if (effectiveTraversePlan == null) {
      throw TraverseException.withMessage("Cannot to build effective traverse plan of type {} to object handle of class {}",
          traversePlan.type(), source.getClass().getCanonicalName());
    }
    return effectiveTraversePlan.execute(source, this);
  }

  @Override
  public Object execute(
      MoveObjectHandleThruTransition1TraversePlan traversePlan, Object source, Object qualifier
  ) throws TraverseException {
    EffectiveTraversePlan effectiveTraversePlan = getEffectiveTraversePlanFor(
        traversePlan,
        source,
        aClass -> traverseAnalyzer.buildEffectiveTaskPlanFor(traversePlan, aClass)
    );
    if (effectiveTraversePlan == null) {
      throw TraverseException.withMessage("Cannot to build effective traverse plan of type {} to object handle of class {}",
          traversePlan.type(), source.getClass().getCanonicalName());
    }
    return effectiveTraversePlan.execute(source, qualifier, this);
  }

  private EffectiveTraversePlan getEffectiveTraversePlanFor(
      MoveObjectHandleThruTransitionTraversePlan traversePlan,
      Object objectHandle,
      Function<Class<?>, Optional<EffectiveTraversePlan>> effectiveTaskPlanBuilder
  ) {
    if (objectHandle.getClass() != traversePlan.objectHandleClass() && !traversePlan.objectHandleClass().isAssignableFrom(objectHandle.getClass())) {
      throw UnexpectedViolationException.withMessage("Traverse plan of type {} expected object handle of class {} or it subclass",
          traversePlan.objectHandleClass());
    }
    if (!ObjectFunctions.isObjectHandleClass(objectHandle.getClass())) {
      throw UnexpectedViolationException.withMessage("Traverse plan of type {} expected object handle to input",
          traversePlan.type());
    }

    Map<Class<?>, EffectiveTraversePlan> effectiveTraversePlans = traversePlan.effectiveTaskPlans();
    EffectiveTraversePlan effectiveTraversePlan = effectiveTraversePlans.get(traversePlan.objectHandleClass());
    if (effectiveTraversePlan == null) {
      effectiveTraversePlan = effectiveTraversePlans.get(objectHandle.getClass());
    }
    if (effectiveTraversePlan == null) {
      effectiveTraversePlan = effectiveTaskPlanBuilder.apply(traversePlan.objectHandleClass()).orElse(null);
      if (effectiveTraversePlan != null) {
        traversePlan.addEffectiveTaskPlan(traversePlan.objectHandleClass(), effectiveTraversePlan);
      } else {
        effectiveTraversePlan = effectiveTaskPlanBuilder.apply(objectHandle.getClass()).orElse(null);
        if (effectiveTraversePlan != null) {
          traversePlan.addEffectiveTaskPlan(objectHandle.getClass(), effectiveTraversePlan);
        }
      }
    }
    return effectiveTraversePlan;
  }
}
