package tech.intellispaces.framework.core.guide;

import tech.intellispaces.framework.commons.exception.UnexpectedViolationException;
import tech.intellispaces.framework.core.exception.TraverseException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Unit mapper guide.
 *
 * @param <S> mapper source object type.
 */
public class UnitMapper1<S, T, Q> extends AbstractMapper1<S, T, Q> {
  private final String tid;
  private final Object unitInstance;
  private final Method mapperMethod;

  public UnitMapper1(String tid, Object unitInstance, Method mapperMethod) {
    if (mapperMethod.getParameterCount() != 2) {
      throw UnexpectedViolationException.withMessage("Guide should have two parameters: source and one qualifier");
    }
    this.tid = tid;
    this.unitInstance = unitInstance;
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
      return (T) mapperMethod.invoke(unitInstance, source, qualifier);
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      throw TraverseException.withCauseAndMessage(e, "Failed to invoke guide method {} in unit {}",
          mapperMethod.getName(), mapperMethod.getDeclaringClass().getCanonicalName());
    }
  }
}
