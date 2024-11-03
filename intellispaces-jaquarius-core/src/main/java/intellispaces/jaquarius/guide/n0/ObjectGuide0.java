package intellispaces.jaquarius.guide.n0;

import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.common.javastatement.method.MethodParam;
import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.jaquarius.exception.TraverseException;
import intellispaces.jaquarius.guide.GuideForm;
import intellispaces.jaquarius.guide.GuideLogger;
import intellispaces.jaquarius.system.ObjectHandleWrapper;

import java.util.stream.Collectors;

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
      return (R) source.$innerHandle().getGuideAction(channelIndex).asAction1().execute(source);
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
      return source.$innerHandle().getGuideAction(channelIndex).asAction1().executeReturnInt(source);
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
      return source.$innerHandle().getGuideAction(channelIndex).asAction1().executeReturnDouble(source);
    } catch (TraverseException e) {
      throw e;
    } catch (Exception e) {
      throw TraverseException.withCauseAndMessage(e, "Failed to invoke guide method ''{0}'' of object handle {1}",
          guideMethod.name(), objectHandleClass.getCanonicalName());
    }
  }

  @Override
  public String toString() {
    return "ObjectGuide0{" +
        "objectHandleClass=" + objectHandleClass +
        ", cid='" + cid + '\'' +
        ", guideMethod=" + guideMethod.name() + "(" + guideMethod.params().stream().map(MethodParam::name).collect(Collectors.joining(", ")) + ")" +
        ", guideForm=" + guideForm +
        '}';
  }
}
