package tech.intellispaces.jaquarius.guide.n1;

import tech.intellispaces.jaquarius.guide.GuideForm;
import tech.intellispaces.jaquarius.system.UnitWrapper;
import tech.intellispaces.java.reflection.method.MethodStatement;

public class UnitMapperOfMoving1<S, T, Q> extends UnitGuide1<S, T, Q> implements AbstractMapperOfMoving1<S, T, Q> {

  public UnitMapperOfMoving1(String cid, UnitWrapper unitInstance, MethodStatement guideMethod, int guideOrdinal, GuideForm guideForm) {
    super(cid, unitInstance, guideMethod, guideOrdinal, guideForm);
  }
}
