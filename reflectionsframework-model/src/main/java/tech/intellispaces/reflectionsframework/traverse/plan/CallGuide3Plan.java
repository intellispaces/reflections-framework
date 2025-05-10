package tech.intellispaces.reflectionsframework.traverse.plan;

import tech.intellispaces.reflectionsframework.guide.n3.Guide3;

/**
 * The execution traverse plan to call guide3.
 */
public interface CallGuide3Plan extends ExecutionTraversePlan {

  Guide3<?, ?, ?, ?, ?> guide();
}
