package tech.intellispaces.reflections.framework.guide.n1;

import java.util.stream.Collectors;

import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.exception.TraverseExceptions;
import tech.intellispaces.reflections.framework.guide.GuideLogger;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.system.ReflectionWrapper;
import tech.intellispaces.jstatements.method.MethodParam;
import tech.intellispaces.jstatements.method.MethodStatement;

abstract class ObjectGuide1<S extends ReflectionWrapper, R, Q> implements Guide1<S, R, Q> {
  private final Class<S> reflectionClass;
  private final String cid;
  private final MethodStatement guideMethod;
  private final int traverseOrdinal;
  private final ReflectionForm targetForm;

  ObjectGuide1(
      String cid,
      Class<S> reflectionClass,
      MethodStatement guideMethod,
      int traverseOrdinal,
      ReflectionForm targetForm
  ) {
    if (guideMethod.params().size() != 1) {
      throw UnexpectedExceptions.withMessage("Guide should have one parameter");
    }
    this.cid = cid;
    this.reflectionClass = reflectionClass;
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
  public R traverse(S source, Q qualifier) throws TraverseException {
    try {
      GuideLogger.logCallGuide(guideMethod);
      return (R) source.$broker().guideAction(traverseOrdinal).castToAction2().execute(source, qualifier);
    } catch (TraverseException e) {
      throw e;
    } catch (Exception e) {
      throw TraverseExceptions.withCauseAndMessage(e, "Failed to invoke guide method '{0}' of reflection {1}",
          guideMethod.name(), reflectionClass.getCanonicalName());
    }
  }

  @Override
  public String toString() {
    return "ObjectGuide1{" +
        "reflectionClass=" + reflectionClass +
        ", cid='" + cid + '\'' +
        ", guideMethod=" + guideMethod.name() + "(" + guideMethod.params().stream().map(MethodParam::name).collect(Collectors.joining(", ")) + ")" +
        ", targetForm=" + targetForm +
        '}';
  }
}
