package tech.intellispaces.jaquarius.guide.n3;

import tech.intellispaces.commons.reflection.method.MethodStatement;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForm;
import tech.intellispaces.jaquarius.system.UnitWrapper;

public class UnitMapperOfMoving3<S, T, Q1, Q2, Q3> extends UnitGuide3<S, T, Q1, Q2, Q3> implements AbstractMapperOfMoving3<S, T, Q1, Q2, Q3> {

  public UnitMapperOfMoving3(
      String cid,
      UnitWrapper unitInstance,
      MethodStatement guideMethod,
      int guideOrdinal,
      ObjectReferenceForm targetForm
  ) {
    super(cid, unitInstance, guideMethod, guideOrdinal, targetForm);
  }
}
