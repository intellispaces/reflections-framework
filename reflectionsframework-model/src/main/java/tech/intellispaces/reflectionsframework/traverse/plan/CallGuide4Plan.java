package tech.intellispaces.reflectionsframework.traverse.plan;

import tech.intellispaces.reflectionsframework.guide.n4.Guide4;

/**
 * The execution traverse plan to call guide4.
 */
public interface CallGuide4Plan extends ExecutionTraversePlan {

  Guide4<?, ?, ?, ?, ?, ?> guide();
}
