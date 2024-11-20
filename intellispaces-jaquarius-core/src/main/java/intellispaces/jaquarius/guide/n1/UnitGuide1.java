package intellispaces.jaquarius.guide.n1;

import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.jaquarius.exception.TraverseException;
import intellispaces.jaquarius.exception.TraverseExceptions;
import intellispaces.jaquarius.guide.GuideForm;
import intellispaces.jaquarius.guide.GuideLogger;
import intellispaces.jaquarius.system.UnitWrapper;
import intellispaces.jaquarius.system.kernel.KernelUnitGuide;
import tech.intellispaces.entity.exception.UnexpectedExceptions;

abstract class UnitGuide1<S, R, Q> implements Guide1<S, R, Q>, KernelUnitGuide<S, R> {
  private final String cid;
  private final UnitWrapper unitInstance;
  private final MethodStatement guideMethod;
  private final int guideOrdinal;
  private final GuideForm guideForm;

  UnitGuide1(String cid, UnitWrapper unitInstance, MethodStatement guideMethod, int guideOrdinal, GuideForm guideForm) {
    if (guideMethod.params().size() != 2) {
      throw UnexpectedExceptions.withMessage("Guide method should have two parameters: source and qualifier");
    }
    this.cid = cid;
    this.unitInstance = unitInstance;
    this.guideMethod = guideMethod;
    this.guideOrdinal = guideOrdinal;
    this.guideForm = guideForm;
  }

  @Override
  public MethodStatement guideMethod() {
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
  public R traverse(S source, Q qualifier) throws TraverseException {
    try {
      GuideLogger.logCallGuide(guideMethod);
      return (R) unitInstance.$unit().getGuideAction(guideOrdinal).castToAction3().execute(unitInstance, source, qualifier);
    } catch (TraverseException e) {
      throw e;
    } catch (Exception e) {
      throw TraverseExceptions.withCauseAndMessage(e, "Failed to invoke unit guide {0} in unit {1}",
          guideMethod.name(), guideMethod.owner().canonicalName());
    }
  }
}
