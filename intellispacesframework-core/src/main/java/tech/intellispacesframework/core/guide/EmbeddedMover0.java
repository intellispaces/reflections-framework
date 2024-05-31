package tech.intellispacesframework.core.guide;

import tech.intellispacesframework.commons.exception.UnexpectedViolationException;
import tech.intellispacesframework.core.exception.TraverseException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Mover guide embedded to object handle.
 *
 * @param <S> mover source object type.
 */
public class EmbeddedMover0<S> extends AbstractMover0<S> {
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
  public S move(S source) throws TraverseException {
    try {
      GuideLogger.logCallGuide(moverMethod);
      return (S) moverMethod.invoke(source);
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      throw TraverseException.withCauseAndMessage(e, "Failed to invoke mover method {} of object handle {}",
          moverMethod.getName(), objectHandleClass.getCanonicalName());
    }
  }
}
