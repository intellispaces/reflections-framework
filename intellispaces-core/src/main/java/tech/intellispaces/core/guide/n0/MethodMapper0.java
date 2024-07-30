package tech.intellispaces.core.guide.n0;

import tech.intellispaces.commons.exception.UnexpectedViolationException;
import tech.intellispaces.core.exception.TraverseException;
import tech.intellispaces.core.guide.GuideLogger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Non-parameterized unit method mapper.
 *
 * @param <S> source object handle type.
 */
public class MethodMapper0<S, T> implements AbstractMapper0<S, T> {
  private final String tid;
  private final Object unitInstance;
  private final Method mapperMethod;

  public MethodMapper0(String tid, Object unitInstance, Method mapperMethod) {
    if (mapperMethod.getParameterCount() != 1) {
      throw UnexpectedViolationException.withMessage("Guide method should have one parameter: source");
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
