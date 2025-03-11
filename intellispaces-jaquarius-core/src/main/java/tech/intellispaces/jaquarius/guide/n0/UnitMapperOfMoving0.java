package tech.intellispaces.jaquarius.guide.n0;

import tech.intellispaces.commons.java.reflection.method.MethodStatement;
import tech.intellispaces.jaquarius.object.reference.ObjectForm;
import tech.intellispaces.jaquarius.system.UnitWrapper;

public class UnitMapperOfMoving0<S, T> extends UnitGuide0<S, T> implements AbstractMapperOfMoving0<S, T> {

  public UnitMapperOfMoving0(
      String cid,
      UnitWrapper unitInstance,
      MethodStatement guideMethod,
      int guideOrdinal,
      ObjectForm targetForm
  ) {
    super(cid, unitInstance, guideMethod, guideOrdinal, targetForm);
  }
}
