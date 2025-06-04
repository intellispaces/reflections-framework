package tech.intellispaces.reflections.framework.guide.n0;

import tech.intellispaces.core.Rid;
import tech.intellispaces.javareflection.method.MethodStatement;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.system.UnitWrapper;

public class UnitMapperOfMoving0<S, T> extends UnitGuide0<S, T> implements AbstractMapperOfMoving0<S, T> {

  public UnitMapperOfMoving0(
      Rid cid,
      UnitWrapper unitInstance,
      MethodStatement guideMethod,
      int guideOrdinal,
      Class<S> sourceClass,
      ReflectionForm targetForm
  ) {
    super(cid, unitInstance, guideMethod, guideOrdinal, sourceClass, targetForm);
  }
}
