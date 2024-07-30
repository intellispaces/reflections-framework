package tech.intellispaces.core.guide.n1;

import tech.intellispaces.commons.exception.UnexpectedViolationException;
import tech.intellispaces.core.exception.TraverseException;
import tech.intellispaces.core.guide.GuideLogger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Attached to object handle mapper guide.<p/>
 *
 * Attached guide can be used exclusively with this object handle only.
 *
 * @param <S> mapper source object type.
 * @param <Q> mapper qualified type.
 */
public class AttachedMapper1<S, T, Q> implements AbstractMapper1<S, T, Q> {
  private final Class<S> objectHandleClass;
  private final String tid;
  private final Method mapperMethod;
  private final Method actualMapperMethod;

  public AttachedMapper1(String tid, Class<S> objectHandleClass, Method mapperMethod, Method actualMapperMethod) {
    if (mapperMethod.getParameterCount() != 1) {
      throw UnexpectedViolationException.withMessage("Guide should have 1 parameter");
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
  public T map(S source, Q qualifier) throws TraverseException {
    try {
      GuideLogger.logCallGuide(mapperMethod);
      return (T) actualMapperMethod.invoke(source, qualifier);
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
