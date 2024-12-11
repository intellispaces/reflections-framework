package tech.intellispaces.jaquarius.traverse.plan;

import tech.intellispaces.jaquarius.guide.n1.Guide1;

/**
 * The execution traverse plan to call guide1.
 */
public interface CallGuide1Plan extends ExecutionTraversePlan {

  Guide1<?, ?, ?> guide();
}
