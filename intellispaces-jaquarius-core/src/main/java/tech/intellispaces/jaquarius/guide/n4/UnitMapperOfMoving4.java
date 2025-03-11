package tech.intellispaces.jaquarius.guide.n4;

import tech.intellispaces.commons.java.reflection.method.MethodStatement;
import tech.intellispaces.jaquarius.object.reference.ObjectForm;
import tech.intellispaces.jaquarius.system.UnitWrapper;

public class UnitMapperOfMoving4<S, T, Q1, Q2, Q3, Q4>
    extends UnitGuide4<S, T, Q1, Q2, Q3, Q4>
    implements AbstractMapperOfMoving4<S, T, Q1, Q2, Q3, Q4>
{
  public UnitMapperOfMoving4(
      String cid,
      UnitWrapper unitInstance,
      MethodStatement guideMethod,
      int guideOrdinal,
      ObjectForm targetForm
  ) {
    super(cid, unitInstance, guideMethod, guideOrdinal, targetForm);
  }
}
