package tech.intellispaces.jaquarius.guide.n1;

import tech.intellispaces.entity.exception.UnexpectedExceptions;
import tech.intellispaces.jaquarius.exception.TraverseException;
import tech.intellispaces.jaquarius.exception.TraverseExceptions;
import tech.intellispaces.jaquarius.guide.GuideLogger;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForm;
import tech.intellispaces.jaquarius.system.ObjectHandleWrapper;
import tech.intellispaces.java.reflection.method.MethodParam;
import tech.intellispaces.java.reflection.method.MethodStatement;

import java.util.stream.Collectors;

abstract class ObjectGuide1<S extends ObjectHandleWrapper, R, Q> implements Guide1<S, R, Q> {
  private final Class<S> objectHandleClass;
  private final String cid;
  private final MethodStatement guideMethod;
  private final int channelIndex;
  private final ObjectReferenceForm targetForm;

  ObjectGuide1(
      String cid,
      Class<S> objectHandleClass,
      MethodStatement guideMethod,
      int channelIndex,
      ObjectReferenceForm targetForm
  ) {
    if (guideMethod.params().size() != 1) {
      throw UnexpectedExceptions.withMessage("Guide should have one parameter");
    }
    this.cid = cid;
    this.objectHandleClass = objectHandleClass;
    this.guideMethod = guideMethod;
    this.channelIndex = channelIndex;
    this.targetForm = targetForm;
  }

  @Override
  public String cid() {
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
      return (R) source.$objectHandleInstance().getGuideAction(channelIndex).castToAction2().execute(source, qualifier);
    } catch (TraverseException e) {
      throw e;
    } catch (Exception e) {
      throw TraverseExceptions.withCauseAndMessage(e, "Failed to invoke guide method '{0}' of object handle {1}",
          guideMethod.name(), objectHandleClass.getCanonicalName());
    }
  }

  @Override
  public String toString() {
    return "ObjectGuide1{" +
        "objectHandleClass=" + objectHandleClass +
        ", cid='" + cid + '\'' +
        ", guideMethod=" + guideMethod.name() + "(" + guideMethod.params().stream().map(MethodParam::name).collect(Collectors.joining(", ")) + ")" +
        ", targetForm=" + targetForm +
        '}';
  }
}
