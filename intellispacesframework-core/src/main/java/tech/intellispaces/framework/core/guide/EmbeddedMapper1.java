package tech.intellispaces.framework.core.guide;

import tech.intellispaces.framework.commons.exception.UnexpectedViolationException;
import tech.intellispaces.framework.core.exception.TraverseException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Embedded mapper guide.
 *
 * @param <S> mapper source object type.
 * @param <Q> mapper qualified type.
 */
public class EmbeddedMapper1<S, T, Q> extends AbstractMapper1<S, T, Q> {
  private final Class<S> objectHandleClass;
  private final String tid;
  private final Method mapperMethod;

  public EmbeddedMapper1(String tid, Class<S> objectHandleClass, Method mapperMethod) {
    if (mapperMethod.getParameterCount() != 1) {
      throw UnexpectedViolationException.withMessage("Guide should have 1 parameter");
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
  public T map(S source, Q qualifier) throws TraverseException {
    try {
      GuideLogger.logCallGuide(mapperMethod);
      return (T) mapperMethod.invoke(source, qualifier);
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      throw TraverseException.withCauseAndMessage(e, "Failed to invoke embedded mapper method {} of object handle {}",
          mapperMethod.getName(), objectHandleClass.getCanonicalName());
    }
  }
}
