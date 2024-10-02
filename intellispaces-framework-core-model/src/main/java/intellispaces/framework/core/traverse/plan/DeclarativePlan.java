package intellispaces.framework.core.traverse.plan;

/**
 * Declarative traverse plan.
 *
 * <p>Declarative traverse plan says what needs to be done, but does not say how to do it.
 */
public interface DeclarativePlan extends TraversePlan {

  @Override
  default boolean isDeclarative() {
    return true;
  }
}
