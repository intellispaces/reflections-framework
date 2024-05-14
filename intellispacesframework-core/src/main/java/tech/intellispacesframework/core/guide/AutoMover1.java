package tech.intellispacesframework.core.guide;

import tech.intellispacesframework.commons.exception.CoveredCheckedException;
import tech.intellispacesframework.core.exception.TraverseException;
import tech.intellispacesframework.core.guide.n1.Mover1;
import tech.intellispacesframework.core.traverse.DeclarativeTraversePlan;
import tech.intellispacesframework.core.traverse.TraverseExecutor;

import java.util.function.BiConsumer;

/**
 * One-parametrized automatic mover guide.
 *
 * <p>Automatic guide builds the traverse plan itself.
 *
 * @param <S> source object type.
 * @param <Q> qualifier type.
 */
public class AutoMover1<S, Q> implements Mover1<S, Q> {
  private final TraverseExecutor traverseExecutor;
  private final DeclarativeTraversePlan declarativeTaskPlan;

  public AutoMover1(DeclarativeTraversePlan declarativeTaskPlan, TraverseExecutor traverseExecutor) {
    this.declarativeTaskPlan = declarativeTaskPlan;
    this.traverseExecutor = traverseExecutor;
  }

  @Override
  @SuppressWarnings("unchecked")
  public S move(S source, Q qualifier) throws TraverseException {
    return (S) declarativeTaskPlan.execute(source, qualifier, traverseExecutor);
  }

  @Override
  public BiConsumer<S, Q> asBiConsumer() {
    return (source, qualifier) -> {
      try {
        move(source, qualifier);
      } catch (TraverseException e) {
        throw CoveredCheckedException.withCause(e);
      }
    };
  }
}
