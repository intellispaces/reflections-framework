package tech.intellispaces.reflections.traverse.plan;

import tech.intellispaces.reflections.guide.n2.Guide2;

/**
 * The execution traverse plan to call guide2.
 */
public interface CallGuide2Plan extends ExecutionTraversePlan {

  Guide2<?, ?, ?, ?> guide();
}
