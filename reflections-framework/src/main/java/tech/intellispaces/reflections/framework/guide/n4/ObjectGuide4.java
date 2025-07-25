package tech.intellispaces.reflections.framework.guide.n4;

import java.util.stream.Collectors;

import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.core.Rid;
import tech.intellispaces.javareflection.method.MethodParam;
import tech.intellispaces.javareflection.method.MethodStatement;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.exception.TraverseExceptions;
import tech.intellispaces.reflections.framework.guide.GuideLogger;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.system.ReflectionWrapper;

abstract class ObjectGuide4<S extends ReflectionWrapper, R, Q1, Q2, Q3, Q4> implements SystemGuide4<S, R, Q1, Q2, Q3, Q4> {
  private final Class<S> reflectionClass;
  private final Rid cid;
  private final MethodStatement guideMethod;
  private final int traverseOrdinal;
  private final ReflectionForm targetForm;

  ObjectGuide4(
      Rid cid,
      Class<S> reflectionClass,
      MethodStatement guideMethod,
      int traverseOrdinal,
      ReflectionForm targetForm
  ) {
    if (guideMethod.params().size() != 4) {
      throw UnexpectedExceptions.withMessage("Guide should have four qualifiers");
    }
    this.cid = cid;
    this.reflectionClass = reflectionClass;
    this.guideMethod = guideMethod;
    this.traverseOrdinal = traverseOrdinal;
    this.targetForm = targetForm;
  }

  @Override
  public Rid channelId() {
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
  public R traverse(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Q4 qualifier4) throws TraverseException {
    try {
      GuideLogger.logCallGuide(guideMethod);
      return (R) source.$handle().guideAction(traverseOrdinal).castToAction5().execute(
          source, qualifier1, qualifier2, qualifier3, qualifier4
      );
    } catch (TraverseException e) {
      throw e;
    } catch (Exception e) {
      throw TraverseExceptions.withCauseAndMessage(e, "Failed to invoke guide method '{0}' of reflection {1}",
          guideMethod.name(), reflectionClass.getCanonicalName());
    }
  }

  @Override
  public String toString() {
    return "ObjectGuide4{" +
        "reflectionClass=" + reflectionClass +
        ", cid='" + cid + '\'' +
        ", guideMethod=" + guideMethod.name() + "(" + guideMethod.params().stream().map(MethodParam::name).collect(Collectors.joining(", ")) + ")" +
        ", targetForm=" + targetForm +
        '}';
  }
}
