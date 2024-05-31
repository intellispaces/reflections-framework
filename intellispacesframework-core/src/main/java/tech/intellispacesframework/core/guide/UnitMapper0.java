package tech.intellispacesframework.core.guide;

import tech.intellispacesframework.commons.exception.UnexpectedViolationException;
import tech.intellispacesframework.core.exception.TraverseException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Unit mapper guide.
 *
 * @param <S> mapper source object type.
 */
public class UnitMapper0<S, T> extends AbstractMapper0<S, T> {
  private final String tid;
  private final Object unitInstance;
  private final Method mapperMethod;

  public UnitMapper0(String tid, Object unitInstance, Method mapperMethod) {
    if (mapperMethod.getParameterCount() != 1) {
      throw UnexpectedViolationException.withMessage("Guide should have one parameter: source");
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
  public T map(S source) throws TraverseException {
    try {
      GuideLogger.logCallGuide(mapperMethod);
      return (T) mapperMethod.invoke(unitInstance, source);
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      throw TraverseException.withCauseAndMessage(e, "Failed to invoke guide method {} in unit {}",
          mapperMethod.getName(), mapperMethod.getDeclaringClass().getCanonicalName());
    }
  }
}
