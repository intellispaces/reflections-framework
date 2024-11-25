package tech.intellispaces.jaquarius.traverse.plan;

/**
 * Execution traverse plan.
 *
 * <p>Execution traverse plan contains instructions how traverse source object in semantic space.
 */
public interface ExecutionTraversePlan extends TraversePlan {

  @Override
  default boolean isDeclarative() {
    return false;
  }
}
