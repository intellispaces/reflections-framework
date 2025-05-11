package tech.intellispaces.reflections.framework.traverse.plan;

import tech.intellispaces.reflections.framework.guide.n0.Guide0;

/**
 * The execution traverse plan to call guide0.
 */
public interface CallGuide0Plan extends ExecutionTraversePlan {

  Guide0<?, ?> guide();
}
