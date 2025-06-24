package tech.intellispaces.reflections.framework.task.plan;

import tech.intellispaces.reflections.framework.guide.SystemGuide;

/**
 * The execution traverse plan to call system guide.
 */
public interface CallGuidePlan extends ExecutionTraversePlan {

  SystemGuide<Object, Object> guide();
}
