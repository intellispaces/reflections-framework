package tech.intellispaces.jaquarius.guide.n3;

import tech.intellispaces.jaquarius.guide.GuideForm;
import tech.intellispaces.jaquarius.system.UnitWrapper;
import tech.intellispaces.java.reflection.method.MethodStatement;

public class UnitMapperOfMoving3<S, T, Q1, Q2, Q3> extends UnitGuide3<S, T, Q1, Q2, Q3> implements AbstractMapperOfMoving3<S, T, Q1, Q2, Q3> {

  public UnitMapperOfMoving3(String cid, UnitWrapper unitInstance, MethodStatement guideMethod, int guideOrdinal, GuideForm guideForm) {
    super(cid, unitInstance, guideMethod, guideOrdinal, guideForm);
  }
}
