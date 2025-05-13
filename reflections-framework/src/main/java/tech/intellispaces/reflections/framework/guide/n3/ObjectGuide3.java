package tech.intellispaces.reflections.framework.guide.n3;

import java.util.stream.Collectors;

import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.exception.TraverseExceptions;
import tech.intellispaces.reflections.framework.guide.GuideLogger;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.system.ObjectHandleWrapper;
import tech.intellispaces.jstatements.method.MethodParam;
import tech.intellispaces.jstatements.method.MethodStatement;

abstract class ObjectGuide3<S extends ObjectHandleWrapper, R, Q1, Q2, Q3> implements Guide3<S, R, Q1, Q2, Q3> {
  private final Class<S> objectHandleClass;
  private final String cid;
  private final MethodStatement guideMethod;
  private final int traverseOrdinal;
  private final ReflectionForm targetForm;

  ObjectGuide3(
      String cid,
      Class<S> objectHandleClass,
      MethodStatement guideMethod,
      int traverseOrdinal,
      ReflectionForm targetForm
  ) {
    if (guideMethod.params().size() != 3) {
      throw UnexpectedExceptions.withMessage("Guide should have three qualifiers");
    }
    this.cid = cid;
    this.objectHandleClass = objectHandleClass;
    this.guideMethod = guideMethod;
    this.traverseOrdinal = traverseOrdinal;
    this.targetForm = targetForm;
  }

  @Override
  public String channelId() {
    return cid;
  }

  @Override
  public ReflectionForm targetForm() {
    return targetForm;
  }

  @Override
  @SuppressWarnings("unchecked")
  public R traverse(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3) throws TraverseException {
    try {
      GuideLogger.logCallGuide(guideMethod);
      return (R) source.$broker().guideAction(traverseOrdinal).castToAction4().execute(
          source, qualifier1, qualifier2, qualifier3
      );
    } catch (TraverseException e) {
      throw e;
    } catch (Exception e) {
      throw TraverseExceptions.withCauseAndMessage(e, "Failed to invoke guide method '{0}' of object handle {1}",
          guideMethod.name(), objectHandleClass.getCanonicalName());
    }
  }

  @Override
  public String toString() {
    return "ObjectGuide3{" +
        "objectHandleClass=" + objectHandleClass +
        ", cid='" + cid + '\'' +
        ", guideMethod=" + guideMethod.name() + "(" + guideMethod.params().stream().map(MethodParam::name).collect(Collectors.joining(", ")) + ")" +
        ", targetForm=" + targetForm +
        '}';
  }
}
