package tech.intellispaces.reflections.framework.guide.n4;

import tech.intellispaces.reflections.framework.object.reference.ObjectReferenceForm;
import tech.intellispaces.reflections.framework.system.UnitWrapper;
import tech.intellispaces.jstatements.method.MethodStatement;

public class UnitMapperOfMoving4<S, T, Q1, Q2, Q3, Q4>
    extends UnitGuide4<S, T, Q1, Q2, Q3, Q4>
    implements AbstractMapperOfMoving4<S, T, Q1, Q2, Q3, Q4>
{
  public UnitMapperOfMoving4(
      String cid,
      UnitWrapper unitInstance,
      MethodStatement guideMethod,
      int guideOrdinal,
      ObjectReferenceForm targetForm
  ) {
    super(cid, unitInstance, guideMethod, guideOrdinal, targetForm);
  }
}
