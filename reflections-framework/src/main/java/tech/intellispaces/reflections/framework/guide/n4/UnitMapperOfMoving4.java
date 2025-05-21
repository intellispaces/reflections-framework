package tech.intellispaces.reflections.framework.guide.n4;

import tech.intellispaces.javareflection.method.MethodStatement;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.system.UnitWrapper;

public class UnitMapperOfMoving4<S, T, Q1, Q2, Q3, Q4>
    extends UnitGuide4<S, T, Q1, Q2, Q3, Q4>
    implements AbstractMapperOfMoving4<S, T, Q1, Q2, Q3, Q4>
{
  public UnitMapperOfMoving4(
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
