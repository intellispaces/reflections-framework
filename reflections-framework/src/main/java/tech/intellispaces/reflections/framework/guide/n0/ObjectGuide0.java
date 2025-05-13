package tech.intellispaces.reflections.framework.guide.n0;

import java.util.stream.Collectors;

import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.exception.TraverseExceptions;
import tech.intellispaces.reflections.framework.guide.GuideLogger;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.system.ObjectHandleWrapper;
import tech.intellispaces.jstatements.method.MethodParam;
import tech.intellispaces.jstatements.method.MethodStatement;

abstract class ObjectGuide0<S extends ObjectHandleWrapper, R> implements Guide0<S, R> {
  private final Class<S> objectHandleClass;
  private final String cid;
  private final ReflectionForm targetForm;
  private final MethodStatement guideMethod;
  private final int traverseOrdinal;

  ObjectGuide0(
    String cid,
    Class<S> objectHandleClass,
    ReflectionForm targetForm,
    MethodStatement guideMethod,
    int traverseOrdinal
  ) {
    if (!guideMethod.params().isEmpty()) {
      throw UnexpectedExceptions.withMessage("Guide should not have parameters");
    }
    this.cid = cid;
    this.objectHandleClass = objectHandleClass;
    this.targetForm = targetForm;
    this.guideMethod = guideMethod;
    this.traverseOrdinal = traverseOrdinal;
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
  public R traverse(S source) throws TraverseException {
    try {
      GuideLogger.logCallGuide(guideMethod);
      return (R) source.$broker().guideAction(traverseOrdinal).castToAction1().execute(source);
    } catch (TraverseException e) {
      throw e;
    } catch (Exception e) {
      throw TraverseExceptions.withCauseAndMessage(e, "Failed to invoke guide method '{0}' of object handle {1}",
        guideMethod.name(), objectHandleClass.getCanonicalName());
    }
  }

  @Override
  public int traverseToInt(S source) throws TraverseException {
    try {
      GuideLogger.logCallGuide(guideMethod);
      return source.$broker().guideAction(traverseOrdinal).castToAction1().executeReturnInt(source);
    } catch (TraverseException e) {
      throw e;
    } catch (Exception e) {
      throw TraverseExceptions.withCauseAndMessage(e, "Failed to invoke guide method '{0}' of object handle {1}",
          guideMethod.name(), objectHandleClass.getCanonicalName());
    }
  }

  @Override
  public double traverseToDouble(S source) throws TraverseException {
    try {
      GuideLogger.logCallGuide(guideMethod);
      return source.$broker().guideAction(traverseOrdinal).castToAction1().executeReturnDouble(source);
    } catch (TraverseException e) {
      throw e;
    } catch (Exception e) {
      throw TraverseExceptions.withCauseAndMessage(e, "Failed to invoke guide method '{0}' of object handle {1}",
          guideMethod.name(), objectHandleClass.getCanonicalName());
    }
  }

  @Override
  public String toString() {
    return "ObjectGuide0{" +
        "objectHandleClass=" + objectHandleClass +
        ", cid='" + cid + '\'' +
        ", guideMethod=" + guideMethod.name() + "(" + guideMethod.params().stream().map(MethodParam::name).collect(Collectors.joining(", ")) + ")" +
        ", targetForm=" + targetForm +
        '}';
  }
}
