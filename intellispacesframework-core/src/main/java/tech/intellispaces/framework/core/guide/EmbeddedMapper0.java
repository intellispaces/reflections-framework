package tech.intellispaces.framework.core.guide;

import tech.intellispaces.framework.commons.exception.UnexpectedViolationException;
import tech.intellispaces.framework.core.exception.TraverseException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Embedded mapper guide.
 *
 * @param <S> mapper source object type.
 */
public class EmbeddedMapper0<S, T> extends AbstractMapper0<S, T> {
  private final Class<S> objectHandleClass;
  private final String tid;
  private final Method mapperMethod;

  public EmbeddedMapper0(String tid, Class<S> objectHandleClass, Method mapperMethod) {
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
      throw TraverseException.withCauseAndMessage(e, "Failed to invoke embedded guide method {} of object handle {}",
          mapperMethod.getName(), objectHandleClass.getCanonicalName());
    }
  }
}
