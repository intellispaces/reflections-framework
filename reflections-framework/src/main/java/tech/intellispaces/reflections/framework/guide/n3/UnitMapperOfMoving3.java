package tech.intellispaces.reflections.framework.guide.n3;

import tech.intellispaces.jstatements.method.MethodStatement;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.system.UnitWrapper;

public class UnitMapperOfMoving3<S, T, Q1, Q2, Q3> extends UnitGuide3<S, T, Q1, Q2, Q3> implements AbstractMapperOfMoving3<S, T, Q1, Q2, Q3> {

  public UnitMapperOfMoving3(
      String cid,
      UnitWrapper unitInstance,
      MethodStatement guideMethod,
      int guideOrdinal,
      ReflectionForm targetForm
  ) {
    super(cid, unitInstance, guideMethod, guideOrdinal, targetForm);
  }
}
