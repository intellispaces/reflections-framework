package tech.intellispaces.reflectionsframework.traverse.plan;

import tech.intellispaces.reflectionsframework.guide.n2.Guide2;

/**
 * The execution traverse plan to call guide2.
 */
public interface CallGuide2Plan extends ExecutionTraversePlan {

  Guide2<?, ?, ?, ?> guide();
}
