package tech.intellispaces.framework.core.guide.n2;

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
 * @param <S> source object type.
 * @param <Q1> first qualifier type.
 * @param <Q2> second qualifier type.
 */
public class AttachedMapper2<S, T, Q1, Q2> implements AbstractMapper2<S, T, Q1, Q2> {
  private final Class<S> objectHandleClass;
  private final String tid;
  private final Method mapperMethod;
  private final Method actualMapperMethod;

  public AttachedMapper2(String tid, Class<S> objectHandleClass, Method mapperMethod, Method actualMapperMethod) {
    if (mapperMethod.getParameterCount() != 2) {
      throw UnexpectedViolationException.withMessage("Guide should have 2 qualifiers");
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
  public T map(S source, Q1 qualifier1, Q2 qualifier2) throws TraverseException {
    try {
      GuideLogger.logCallGuide(mapperMethod);
      return (T) actualMapperMethod.invoke(source, qualifier1, qualifier2);
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
