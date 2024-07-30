package tech.intellispaces.core.guide.n2;

import tech.intellispaces.commons.exception.UnexpectedViolationException;
import tech.intellispaces.core.exception.TraverseException;
import tech.intellispaces.core.guide.GuideLogger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Attached to object handle mover guide.<p/>
 *
 * Attached guide can be used exclusively with this object handle only.
 *
 * @param <S> source type.
 * @param <B> backward object type.
 * @param <Q1> first qualifier type.
 * @param <Q2> second qualifier type.
 */
public class AttachedMover2<S, B, Q1, Q2> implements AbstractMover2<S, B, Q1, Q2> {
  private final Class<S> objectHandleClass;
  private final String tid;
  private final Method moverMethod;
  private final Method actualMoverMethod;

  public AttachedMover2(String tid, Class<S> objectHandleClass, Method moverMethod, Method actualMoverMethod) {
    if (moverMethod.getParameterCount() != 2) {
      throw UnexpectedViolationException.withMessage("Attached guide should have 2 qualifiers");
    }
    this.tid = tid;
    this.objectHandleClass = objectHandleClass;
    this.moverMethod = moverMethod;
    this.actualMoverMethod = actualMoverMethod;
  }

  @Override
  public String tid() {
    return tid;
  }

  @Override
  @SuppressWarnings("unchecked")
  public B move(S source, Q1 qualifier1, Q2 qualifier2) throws TraverseException {
    try {
      GuideLogger.logCallGuide(moverMethod);
      return (B) actualMoverMethod.invoke(source, qualifier1, qualifier2);
    } catch (InvocationTargetException e) {
      Throwable t = e.getTargetException();
      if (t instanceof TraverseException) {
        throw (TraverseException) t;
      } else {
        throw TraverseException.withCauseAndMessage(e, "Failed to invoke attached mover method {} of object handle {}",
            actualMoverMethod.getName(), objectHandleClass.getCanonicalName());
      }
    } catch (IllegalAccessException | IllegalArgumentException e) {
      throw TraverseException.withCauseAndMessage(e, "Failed to invoke attached mover method {} of object handle {}",
          actualMoverMethod.getName(), objectHandleClass.getCanonicalName());
    }
  }
}
