package tech.intellispaces.jaquarius.guide.n1;

import tech.intellispaces.commons.java.reflection.method.MethodStatement;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForm;
import tech.intellispaces.jaquarius.system.UnitWrapper;

public class UnitMapperOfMoving1<S, T, Q> extends UnitGuide1<S, T, Q> implements AbstractMapperOfMoving1<S, T, Q> {

  public UnitMapperOfMoving1(
      String cid,
      UnitWrapper unitInstance,
      MethodStatement guideMethod,
      int guideOrdinal,
      ObjectReferenceForm targetForm
  ) {
    super(cid, unitInstance, guideMethod, guideOrdinal, targetForm);
  }
}
