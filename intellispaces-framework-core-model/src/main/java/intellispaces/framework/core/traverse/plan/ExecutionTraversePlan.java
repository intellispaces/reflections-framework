package intellispaces.framework.core.traverse.plan;

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
