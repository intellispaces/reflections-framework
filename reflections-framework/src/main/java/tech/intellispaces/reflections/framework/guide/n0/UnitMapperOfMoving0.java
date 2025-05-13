package tech.intellispaces.reflections.framework.guide.n0;

import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.system.UnitWrapper;
import tech.intellispaces.jstatements.method.MethodStatement;

public class UnitMapperOfMoving0<S, T> extends UnitGuide0<S, T> implements AbstractMapperOfMoving0<S, T> {

  public UnitMapperOfMoving0(
      String cid,
      UnitWrapper unitInstance,
      MethodStatement guideMethod,
      int guideOrdinal,
      ReflectionForm targetForm
  ) {
    super(cid, unitInstance, guideMethod, guideOrdinal, targetForm);
  }
}
