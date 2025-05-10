package tech.intellispaces.reflections.framework.traverse.plan;

import tech.intellispaces.reflections.framework.guide.n2.Guide2;

/**
 * The execution traverse plan to call guide2.
 */
public interface CallGuide2Plan extends ExecutionTraversePlan {

  Guide2<?, ?, ?, ?> guide();
}
