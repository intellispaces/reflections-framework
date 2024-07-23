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
public class AttachedMapper0<S, T> implements AbstractMapper0<S, T> {
  private final Class<S> objectHandleClass;
  private final String tid;
  private final Method mapperMethod;
  private final Method actualMapperMethod;

  public AttachedMapper0(String tid, Class<S> objectHandleClass, Method mapperMethod, Method actualMapperMethod) {
    if (mapperMethod.getParameterCount() != 0) {
      throw UnexpectedViolationException.withMessage("Guide should not have parameters");
    }
    this.tid = tid;
    this.objectHandleClass = objectHandleClass;
    this.mapperMethod = mapperMethod;
    this.actualMapperMethod = actualMapperMethod;
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
      return (T) actualMapperMethod.invoke(source);
    } catch (InvocationTargetException e) {
      Throwable t = e.getTargetException();
      if (t instanceof TraverseException) {
        throw (TraverseException) t;
      } else {
        throw TraverseException.withCauseAndMessage(e, "Failed to invoke attached mapper method {} of object handle {}",
            actualMapperMethod.getName(), objectHandleClass.getCanonicalName());
      }
    } catch (IllegalAccessException | IllegalArgumentException e) {
      throw TraverseException.withCauseAndMessage(e, "Failed to invoke attached mapper method {} of object handle {}",
          actualMapperMethod.getName(), objectHandleClass.getCanonicalName());
    }
  }
}
