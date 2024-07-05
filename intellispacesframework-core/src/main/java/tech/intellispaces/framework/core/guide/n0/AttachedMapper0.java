package tech.intellispaces.framework.core.guide.n0;

import tech.intellispaces.framework.commons.exception.UnexpectedViolationException;
import tech.intellispaces.framework.core.exception.TraverseException;
import tech.intellispaces.framework.core.guide.GuideLogger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Attached to object handle mapper guide.<p/>
 *
 * Attached guide can be used exclusively with this object handle only.
 *
 * @param <S> mapper source object type.
 */
public class AttachedMapper0<S, T> implements BasicMapper0<S, T> {
  private final Class<S> objectHandleClass;
  private final String tid;
  private final Method mapperMethod;

  public AttachedMapper0(String tid, Class<S> objectHandleClass, Method mapperMethod) {
    if (mapperMethod.getParameterCount() != 0) {
      throw UnexpectedViolationException.withMessage("Guide should not have parameters");
    }
    this.tid = tid;
    this.objectHandleClass = objectHandleClass;
    this.mapperMethod = mapperMethod;
  }

  @Override
  public String tid() {
    return tid;
  }

  @Override
  @SuppressWarnings("unchecked")
  public T map(S source) throws TraverseException {
    try {
      GuideLogger.logCallGuide(mapperMethod);
      return (T) mapperMethod.invoke(source);
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      throw TraverseException.withCauseAndMessage(e, "Failed to invoke attached guide method {} of object handle {}",
          mapperMethod.getName(), objectHandleClass.getCanonicalName());
    }
  }
}
