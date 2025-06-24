package tech.intellispaces.reflections.framework.task.plan;

/**
 * The execution traverse plan.
 * <p>
 * The execution traverse plan contains instructions how traverse source reflection in semantic space.
 */
public interface ExecutionTraversePlan extends TraversePlan {

  @Override
  default boolean isDeclarative() {
    return false;
  }
}
