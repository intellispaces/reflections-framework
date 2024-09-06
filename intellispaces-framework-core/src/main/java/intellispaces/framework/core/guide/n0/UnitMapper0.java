package intellispaces.framework.core.guide.n0;

import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.framework.core.exception.TraverseException;
import intellispaces.framework.core.guide.GuideLogger;
import intellispaces.framework.core.system.UnitWrapper;

import java.lang.reflect.Method;

/**
 * Non-parameterized unit method mapper.
 *
 * @param <S> source object handle type.
 */
public class UnitMapper0<S, T> implements AbstractMapper0<S, T> {
  private final String tid;
  private final UnitWrapper unitInstance;
  private final Method guideMethod;
  private final int guideActionIndex;

  public UnitMapper0(String tid, UnitWrapper unitInstance, Method guideMethod, int guideActionIndex) {
    if (guideMethod.getParameterCount() != 1) {
      throw UnexpectedViolationException.withMessage("Guide method should have one parameter: source");
    }
    this.tid = tid;
    this.unitInstance = unitInstance;
    this.guideMethod = guideMethod;
    this.guideActionIndex = guideActionIndex;
  }

  @Override
  public String tid() {
    return tid;
  }

  @Override
  @SuppressWarnings("unchecked")
  public T map(S source) throws TraverseException {
    try {
      GuideLogger.logCallGuide(guideMethod);
      return (T) unitInstance.$unit().getGuideAction(guideActionIndex).asAction1().execute(source);
    } catch (TraverseException e) {
      throw e;
    } catch (Exception e) {
      throw TraverseException.withCauseAndMessage(e, "Failed to invoke unit guide {0} in unit {0}",
          guideMethod.getName(), guideMethod.getDeclaringClass().getCanonicalName());
    }
  }
}
