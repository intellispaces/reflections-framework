package intellispaces.jaquarius.traverse.plan;

import intellispaces.jaquarius.guide.n3.Guide3;

public interface CallGuide3Plan extends ExecutionTraversePlan {

  Guide3<?, ?, ?, ?, ?> guide();
}
