package tech.intellispaces.core.traverse;

/**
 * Actual traverse plan.
 *
 * <p>Actual traverse plan contains instructions how traverse source object in semantic space.
 */
public interface ActualPlan extends TraversePlan {

  @Override
  default boolean isDeclarative() {
    return false;
  }
}
