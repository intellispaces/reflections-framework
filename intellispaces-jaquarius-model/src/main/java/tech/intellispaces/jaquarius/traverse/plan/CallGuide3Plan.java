package tech.intellispaces.jaquarius.traverse.plan;

import tech.intellispaces.jaquarius.guide.n3.Guide3;

/**
 * The execution traverse plan to call guide3.
 */
public interface CallGuide3Plan extends ExecutionTraversePlan {

  Guide3<?, ?, ?, ?, ?> guide();
}
