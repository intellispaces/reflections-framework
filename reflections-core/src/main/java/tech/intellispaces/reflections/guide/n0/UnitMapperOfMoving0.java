package tech.intellispaces.reflections.guide.n0;

import tech.intellispaces.reflections.object.reference.ObjectReferenceForm;
import tech.intellispaces.reflections.system.UnitWrapper;
import tech.intellispaces.jstatements.method.MethodStatement;

public class UnitMapperOfMoving0<S, T> extends UnitGuide0<S, T> implements AbstractMapperOfMoving0<S, T> {

  public UnitMapperOfMoving0(
      String cid,
      UnitWrapper unitInstance,
      MethodStatement guideMethod,
      int guideOrdinal,
      ObjectReferenceForm targetForm
  ) {
    super(cid, unitInstance, guideMethod, guideOrdinal, targetForm);
  }
}
