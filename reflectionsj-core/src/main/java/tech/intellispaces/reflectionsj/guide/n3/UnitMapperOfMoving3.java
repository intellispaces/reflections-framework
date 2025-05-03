package tech.intellispaces.reflectionsj.guide.n3;

import tech.intellispaces.reflectionsj.object.reference.ObjectReferenceForm;
import tech.intellispaces.reflectionsj.system.UnitWrapper;
import tech.intellispaces.statementsj.method.MethodStatement;

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
