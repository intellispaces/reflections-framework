package tech.intellispaces.framework.core.guide;

import tech.intellispaces.framework.commons.exception.UnexpectedViolationException;
import tech.intellispaces.framework.core.exception.TraverseException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Embedded mover guide.
 *
 * @param <S> source object handle type.
 * @param <B> backward object handle type.
 */
public class EmbeddedMover0<S, B> extends AbstractMover0<S, B> {
  private final Class<S> objectHandleClass;
  private final String tid;
  private final Method moverMethod;

  public EmbeddedMover0(String tid, Class<S> objectHandleClass, Method moverMethod) {
    if (moverMethod.getParameterCount() != 0) {
      throw UnexpectedViolationException.withMessage("Embedded guide should not have parameters");
    }
    this.tid = tid;
    this.objectHandleClass = objectHandleClass;
    this.moverMethod = moverMethod;
  }

  @Override
  public String tid() {
    return tid;
  }

  @Override
  @SuppressWarnings("unchecked")
  public B move(S source) throws TraverseException {
    try {
      GuideLogger.logCallGuide(moverMethod);
      return (B) moverMethod.invoke(source);
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      throw TraverseException.withCauseAndMessage(e, "Failed to invoke mover method {} of object handle {}",
          moverMethod.getName(), objectHandleClass.getCanonicalName());
    }
  }
}
