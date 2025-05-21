package tech.intellispaces.reflections.framework.guide.n2;

import java.util.stream.Collectors;

import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.javareflection.method.MethodParam;
import tech.intellispaces.javareflection.method.MethodStatement;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.exception.TraverseExceptions;
import tech.intellispaces.reflections.framework.guide.GuideLogger;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.system.ReflectionWrapper;

abstract class ObjectGuide2<S extends ReflectionWrapper, R, Q1, Q2> implements Guide2<S, R, Q1, Q2> {
  private final Class<S> reflectionClass;
  private final String cid;
  private final MethodStatement guideMethod;
  private final int traverseOrdinal;
  private final ReflectionForm targetForm;

  ObjectGuide2(
      String cid,
      Class<S> reflectionClass,
      MethodStatement guideMethod,
      int traverseOrdinal,
      ReflectionForm targetForm
  ) {
    if (guideMethod.params().size() != 2) {
      throw UnexpectedExceptions.withMessage("Guide should have two qualifiers");
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
  public Class<S> sourceClass() {
    return reflectionClass;
  }

  @Override
  public ReflectionForm targetForm() {
    return targetForm;
  }

  @Override
  @SuppressWarnings("unchecked")
  public R traverse(S source, Q1 qualifier1, Q2 qualifier2) throws TraverseException {
    try {
      GuideLogger.logCallGuide(guideMethod);
      return (R) source.$handle().guideAction(traverseOrdinal).castToAction3().execute(source, qualifier1, qualifier2);
    } catch (TraverseException e) {
      throw e;
    } catch (Exception e) {
      throw TraverseExceptions.withCauseAndMessage(e, "Failed to invoke guide method '{0}' of reflection {1}",
          guideMethod.name(), reflectionClass.getCanonicalName());
    }
  }

  @Override
  public String toString() {
    return "ObjectGuide2{" +
        "reflectionClass=" + reflectionClass +
        ", cid='" + cid + '\'' +
        ", guideMethod=" + guideMethod.name() + "(" + guideMethod.params().stream().map(MethodParam::name).collect(Collectors.joining(", ")) + ")" +
        ", targetForm=" + targetForm +
        '}';
  }
}
