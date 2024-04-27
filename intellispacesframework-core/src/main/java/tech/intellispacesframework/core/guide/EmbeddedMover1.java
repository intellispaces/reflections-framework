package tech.intellispacesframework.core.guide;

import tech.intellispacesframework.commons.exception.UnexpectedViolationException;
import tech.intellispacesframework.core.exception.TraverseException;
import tech.intellispacesframework.core.guide.n1.Mover1;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.BiConsumer;

/**
 * Mover guide embedded to object handle.
 *
 * @param <S> mover source object type.
 * @param <Q> mover qualified type.
 */
public class EmbeddedMover1<S, Q> implements Mover1<S, Q> {
  private final Class<S> objectHandleClass;
  private final Method moverMethod;

  public EmbeddedMover1(Class<S> objectHandleClass, Method moverMethod) {
    this.objectHandleClass = objectHandleClass;
    this.moverMethod = moverMethod;
  }

  @Override
  @SuppressWarnings("unchecked")
  public S move(S source, Q qualifier) throws TraverseException {
    try {
      return (S) moverMethod.invoke(source, qualifier);
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      throw UnexpectedViolationException.withMessage("Failed to invoke mover method {} of object handle {}",
          moverMethod.getName(), objectHandleClass.getCanonicalName());
    }
  }

  @Override
  public void async(S source, Object... qualifiers) {

  }

  @Override
  public BiConsumer<S, Q> asBiConsumer() {
    return (source, qualifier) -> {
      try {
        move(source, qualifier);
      } catch (TraverseException e) {
        throw new RuntimeException(e);
      }
    };
  }
}
