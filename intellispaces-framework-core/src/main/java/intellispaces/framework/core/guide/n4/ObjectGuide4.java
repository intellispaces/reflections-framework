package intellispaces.framework.core.guide.n4;

import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.framework.core.exception.TraverseException;
import intellispaces.framework.core.guide.GuideForm;
import intellispaces.framework.core.guide.GuideLogger;
import intellispaces.framework.core.system.ObjectHandleWrapper;

import java.lang.reflect.Method;

abstract class ObjectGuide4<S extends ObjectHandleWrapper, R, Q1, Q2, Q3, Q4> implements Guide4<S, R, Q1, Q2, Q3, Q4> {
  private final Class<S> objectHandleClass;
  private final String cid;
  private final Method guideMethod;
  private final int channelIndex;
  private final GuideForm guideForm;

  ObjectGuide4(
      String cid,
      Class<S> objectHandleClass,
      Method guideMethod,
      int channelIndex,
      GuideForm guideForm
  ) {
    if (guideMethod.getParameterCount() != 4) {
      throw UnexpectedViolationException.withMessage("Guide should have four qualifiers");
    }
    this.cid = cid;
    this.objectHandleClass = objectHandleClass;
    this.guideMethod = guideMethod;
    this.channelIndex = channelIndex;
    this.guideForm = guideForm;
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
      return (R) source.$innerHandle().getGuideAction(channelIndex).asAction5().execute(
          source, qualifier1, qualifier2, qualifier3, qualifier4
      );
    } catch (TraverseException e) {
      throw e;
    } catch (Exception e) {
      throw TraverseException.withCauseAndMessage(e, "Failed to invoke guide method ''{0}'' of object handle {1}",
          guideMethod.getName(), objectHandleClass.getCanonicalName());
    }
  }
}
