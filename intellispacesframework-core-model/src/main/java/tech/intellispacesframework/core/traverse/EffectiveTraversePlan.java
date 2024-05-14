package tech.intellispacesframework.core.traverse;

/**
 * Prepared effective traverse plan.
 *
 * <p>Effective traverse plan contains instructions how traverse source object in semantic space.
 */
public interface EffectiveTraversePlan extends TraversePlan {

  @Override
  default boolean isDeclarative() {
    return false;
  }
}
