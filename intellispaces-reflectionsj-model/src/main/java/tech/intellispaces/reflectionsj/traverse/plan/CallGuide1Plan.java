package tech.intellispaces.reflectionsj.traverse.plan;

import tech.intellispaces.reflectionsj.guide.n1.Guide1;

/**
 * The execution traverse plan to call guide1.
 */
public interface CallGuide1Plan extends ExecutionTraversePlan {

  Guide1<?, ?, ?> guide();
}
