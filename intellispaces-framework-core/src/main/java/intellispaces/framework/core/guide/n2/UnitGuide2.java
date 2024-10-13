package intellispaces.framework.core.guide.n2;

import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.framework.core.exception.TraverseException;
import intellispaces.framework.core.guide.GuideForm;
import intellispaces.framework.core.guide.GuideLogger;
import intellispaces.framework.core.system.UnitWrapper;
import intellispaces.framework.core.system.kernel.KernelUnitGuide;

import java.lang.reflect.Method;

abstract class UnitGuide2<S, R, Q1, Q2> implements Guide2<S, R, Q1, Q2>, KernelUnitGuide<S, R> {
  private final String cid;
  private final UnitWrapper unitInstance;
  private final Method guideMethod;
  private final int guideOrdinal;
  private final GuideForm guideForm;

  UnitGuide2(String cid, UnitWrapper unitInstance, Method guideMethod, int guideOrdinal, GuideForm guideForm) {
    if (guideMethod.getParameterCount() != 3) {
      throw UnexpectedViolationException.withMessage("Guide method should have three parameters: source and two qualifiers");
    }
    this.cid = cid;
    this.unitInstance = unitInstance;
    this.guideMethod = guideMethod;
    this.guideOrdinal = guideOrdinal;
    this.guideForm = guideForm;
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
  public String cid() {
    return cid;
  }

  @Override
  public GuideForm guideForm() {
    return guideForm;
  }

  @Override
  @SuppressWarnings("unchecked")
  public R traverse(S source, Q1 qualifier1, Q2 qualifier2) throws TraverseException {
    try {
      GuideLogger.logCallGuide(guideMethod);
      return (R) unitInstance.$unit().getGuideAction(guideOrdinal).asAction4().execute(
          unitInstance, source, qualifier1, qualifier2
      );
    } catch (TraverseException e) {
      throw e;
    } catch (Exception e) {
      throw TraverseException.withCauseAndMessage(e, "Failed to invoke unit guide {0} in unit {1}",
          guideMethod.getName(), guideMethod.getDeclaringClass().getCanonicalName());
    }
  }
}
