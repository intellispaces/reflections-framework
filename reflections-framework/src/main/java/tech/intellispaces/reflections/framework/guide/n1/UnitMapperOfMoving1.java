package tech.intellispaces.reflections.framework.guide.n1;

import tech.intellispaces.javareflection.method.MethodStatement;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.system.UnitWrapper;

public class UnitMapperOfMoving1<S, T, Q> extends UnitGuide1<S, T, Q> implements AbstractMapperOfMoving1<S, T, Q> {

  public UnitMapperOfMoving1(
      String cid,
      UnitWrapper unitInstance,
      MethodStatement guideMethod,
      int guideOrdinal,
      Class<S> sourceClass,
      ReflectionForm targetForm
  ) {
    super(cid, unitInstance, guideMethod, guideOrdinal, sourceClass, targetForm);
  }
}
