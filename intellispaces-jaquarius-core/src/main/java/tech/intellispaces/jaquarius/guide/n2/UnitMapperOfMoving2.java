package tech.intellispaces.jaquarius.guide.n2;

import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForm;
import tech.intellispaces.jaquarius.system.UnitWrapper;
import tech.intellispaces.reflection.method.MethodStatement;

public class UnitMapperOfMoving2<S, T, Q1, Q2> extends UnitGuide2<S, T, Q1, Q2> implements AbstractMapperOfMoving2<S, T, Q1, Q2> {

  public UnitMapperOfMoving2(
      String cid,
      UnitWrapper unitInstance,
      MethodStatement guideMethod,
      int guideOrdinal,
      ObjectReferenceForm targetForm
  ) {
    super(cid, unitInstance, guideMethod, guideOrdinal, targetForm);
  }
}
