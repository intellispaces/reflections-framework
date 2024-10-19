package intellispaces.framework.core.guide.n0;

import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.framework.core.exception.TraverseException;
import intellispaces.framework.core.guide.GuideForm;
import intellispaces.framework.core.guide.GuideLogger;
import intellispaces.framework.core.system.ObjectHandleWrapper;

import java.lang.reflect.Method;

abstract class ObjectGuide0<S extends ObjectHandleWrapper, R> implements Guide0<S, R> {
  private final Class<S> objectHandleClass;
  private final String cid;
  private final GuideForm guideForm;
  private final MethodStatement guideMethod;
  private final int channelIndex;

  ObjectGuide0(
    String cid,
    Class<S> objectHandleClass,
    GuideForm guideForm,
    MethodStatement guideMethod,
    int channelIndex
  ) {
    if (!guideMethod.params().isEmpty()) {
      throw UnexpectedViolationException.withMessage("Guide should not have parameters");
    }
    this.cid = cid;
    this.objectHandleClass = objectHandleClass;
    this.guideForm = guideForm;
    this.guideMethod = guideMethod;
    this.channelIndex = channelIndex;
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
  public R traverse(S source) throws TraverseException {
    try {
      GuideLogger.logCallGuide(guideMethod);
      return (R) source.$handle().getGuideAction(channelIndex).asAction1().execute(source);
    } catch (TraverseException e) {
      throw e;
    } catch (Exception e) {
      throw TraverseException.withCauseAndMessage(e, "Failed to invoke guide method ''{0}'' of object handle {1}",
        guideMethod.name(), objectHandleClass.getCanonicalName());
    }
  }

  @Override
  public int traverseToInt(S source) throws TraverseException {
    try {
      GuideLogger.logCallGuide(guideMethod);
      return source.$handle().getGuideAction(channelIndex).asAction1().executeReturnInt(source);
    } catch (TraverseException e) {
      throw e;
    } catch (Exception e) {
      throw TraverseException.withCauseAndMessage(e, "Failed to invoke guide method ''{0}'' of object handle {1}",
          guideMethod.name(), objectHandleClass.getCanonicalName());
    }
  }

  @Override
  public double traverseToDouble(S source) throws TraverseException {
    try {
      GuideLogger.logCallGuide(guideMethod);
      return source.$handle().getGuideAction(channelIndex).asAction1().executeReturnDouble(source);
    } catch (TraverseException e) {
      throw e;
    } catch (Exception e) {
      throw TraverseException.withCauseAndMessage(e, "Failed to invoke guide method ''{0}'' of object handle {1}",
          guideMethod.name(), objectHandleClass.getCanonicalName());
    }
  }
}
