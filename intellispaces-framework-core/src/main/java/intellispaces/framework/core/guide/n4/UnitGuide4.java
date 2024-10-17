package intellispaces.framework.core.guide.n4;

import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.framework.core.exception.TraverseException;
import intellispaces.framework.core.guide.GuideForm;
import intellispaces.framework.core.guide.GuideLogger;
import intellispaces.framework.core.system.UnitWrapper;
import intellispaces.framework.core.system.kernel.KernelUnitGuide;

import java.lang.reflect.Method;

abstract class UnitGuide4<S, R, Q1, Q2, Q3, Q4> implements Guide4<S, R, Q1, Q2, Q3, Q4>, KernelUnitGuide<S, R> {
  private final String cid;
  private final UnitWrapper unitInstance;
  private final Method guideMethod;
  private final int guideOrdinal;
  private final GuideForm guideForm;

  UnitGuide4(String cid, UnitWrapper unitInstance, Method guideMethod, int guideOrdinal, GuideForm guideForm) {
    if (guideMethod.getParameterCount() != 5) {
      throw UnexpectedViolationException.withMessage("Guide method should have four parameters: source and four qualifiers");
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
  public R traverse(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Q4 qualifier4) throws TraverseException {
    try {
      GuideLogger.logCallGuide(guideMethod);
      return (R) unitInstance.$unit().getGuideAction(guideOrdinal).asAction6().execute(
          unitInstance, source, qualifier1, qualifier2, qualifier3, qualifier4
      );
    } catch (TraverseException e) {
      throw e;
    } catch (Exception e) {
      throw TraverseException.withCauseAndMessage(e, "Failed to invoke unit guide {0} in unit {1}",
          guideMethod.getName(), guideMethod.getDeclaringClass().getCanonicalName());
    }
  }
}
