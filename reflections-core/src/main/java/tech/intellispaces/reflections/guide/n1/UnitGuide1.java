package tech.intellispaces.reflections.guide.n1;

import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.reflections.exception.TraverseException;
import tech.intellispaces.reflections.exception.TraverseExceptions;
import tech.intellispaces.reflections.guide.GuideLogger;
import tech.intellispaces.reflections.object.reference.ObjectReferenceForm;
import tech.intellispaces.reflections.system.UnitGuide;
import tech.intellispaces.reflections.system.UnitWrapper;
import tech.intellispaces.jstatements.method.MethodStatement;

abstract class UnitGuide1<S, R, Q> implements Guide1<S, R, Q>, UnitGuide<S, R> {
  private final String cid;
  private final UnitWrapper unit;
  private final MethodStatement guideMethod;
  private final int guideOrdinal;
  private final ObjectReferenceForm targetForm;

  UnitGuide1(String cid, UnitWrapper unit, MethodStatement guideMethod, int guideOrdinal, ObjectReferenceForm targetForm) {
    if (guideMethod.params().size() != 2) {
      throw UnexpectedExceptions.withMessage("Guide method should have two parameters: source and qualifier");
    }
    this.cid = cid;
    this.unit = unit;
    this.guideMethod = guideMethod;
    this.guideOrdinal = guideOrdinal;
    this.targetForm = targetForm;
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
  public String channelId() {
    return cid;
  }

  @Override
  public ObjectReferenceForm targetForm() {
    return targetForm;
  }

  @Override
  @SuppressWarnings("unchecked")
  public R traverse(S source, Q qualifier) throws TraverseException {
    try {
      GuideLogger.logCallGuide(guideMethod);
      return (R) unit.$broker().guideAction(guideOrdinal).castToAction2().execute(source, qualifier);
    } catch (TraverseException e) {
      throw e;
    } catch (Exception e) {
      throw TraverseExceptions.withCauseAndMessage(e, "Failed to invoke unit guide {0} in unit {1}",
          guideMethod.name(), guideMethod.owner().canonicalName());
    }
  }
}
