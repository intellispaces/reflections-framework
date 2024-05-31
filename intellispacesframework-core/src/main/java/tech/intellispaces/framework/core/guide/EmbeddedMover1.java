package tech.intellispaces.framework.core.guide;

import tech.intellispaces.framework.commons.exception.UnexpectedViolationException;
import tech.intellispaces.framework.core.exception.TraverseException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Mover guide embedded to object handle.
 *
 * @param <S> mover source object type.
 * @param <Q> mover qualified type.
 */
public class EmbeddedMover1<S, Q> extends AbstractMover1<S, Q> {
  private final Class<S> objectHandleClass;
  private final String tid;
  private final Method moverMethod;

  public EmbeddedMover1(String tid, Class<S> objectHandleClass, Method moverMethod) {
    if (moverMethod.getParameterCount() != 1) {
      throw UnexpectedViolationException.withMessage("Embedded guide should have 1 parameter");
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
  public S move(S source, Q qualifier) throws TraverseException {
    try {
      GuideLogger.logCallGuide(moverMethod);
      return (S) moverMethod.invoke(source, qualifier);
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      throw TraverseException.withCauseAndMessage(e, "Failed to invoke mover method {} of object handle {}",
          moverMethod.getName(), objectHandleClass.getCanonicalName());
    }
  }
}
