package intellispaces.framework.core.guide.n2;

import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.framework.core.exception.TraverseException;
import intellispaces.framework.core.guide.GuideLogger;
import intellispaces.framework.core.system.UnitWrapper;
import intellispaces.framework.core.system.kernel.KernelUnitGuide;

import java.lang.reflect.Method;

abstract class UnitGuide2<S, R, Q1, Q2> implements Guide2<S, R, Q1, Q2>, KernelUnitGuide<S, R> {
  private final String tid;
  private final UnitWrapper unitInstance;
  private final Method guideMethod;
  private final int guideOrdinal;

  UnitGuide2(String tid, UnitWrapper unitInstance, Method guideMethod, int guideOrdinal) {
    if (guideMethod.getParameterCount() != 3) {
      throw UnexpectedViolationException.withMessage("Guide method should have three parameters: source and two qualifiers");
    }
    this.tid = tid;
    this.unitInstance = unitInstance;
    this.guideMethod = guideMethod;
    this.guideOrdinal = guideOrdinal;
  }

  @Override
  public Method guideMethod() {
    return guideMethod;
  }

  @Override
  public int guideOrdinal() {
    return guideOrdinal;
  }

  @Override
  public String tid() {
    return tid;
  }

  @Override
  @SuppressWarnings("unchecked")
  public R traverse(S source, Q1 qualifier1, Q2 qualifier2) throws TraverseException {
    try {
      GuideLogger.logCallGuide(guideMethod);
      return (R) unitInstance.$unit().getGuideAction(guideOrdinal).asAction3().execute(source, qualifier1, qualifier2);
    } catch (TraverseException e) {
      throw e;
    } catch (Exception e) {
      throw TraverseException.withCauseAndMessage(e, "Failed to invoke unit guide {0} in unit {1}",
          guideMethod.getName(), guideMethod.getDeclaringClass().getCanonicalName());
    }
  }
}
