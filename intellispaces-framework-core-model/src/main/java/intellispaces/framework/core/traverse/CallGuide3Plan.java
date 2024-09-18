package intellispaces.framework.core.traverse;

import intellispaces.framework.core.guide.n3.Guide3;

public interface CallGuide3Plan extends ExecutionPlan {

  Guide3<?, ?, ?, ?, ?> guide();
}
