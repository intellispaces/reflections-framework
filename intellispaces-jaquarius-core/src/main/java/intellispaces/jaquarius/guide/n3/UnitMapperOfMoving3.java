package intellispaces.jaquarius.guide.n3;

import intellispaces.jaquarius.guide.GuideForm;
import intellispaces.jaquarius.system.UnitWrapper;

import java.lang.reflect.Method;

public class UnitMapperOfMoving3<S, T, Q1, Q2, Q3> extends UnitGuide3<S, T, Q1, Q2, Q3> implements AbstractMapperOfMoving3<S, T, Q1, Q2, Q3> {

  public UnitMapperOfMoving3(String cid, UnitWrapper unitInstance, Method guideMethod, int guideOrdinal, GuideForm guideForm) {
    super(cid, unitInstance, guideMethod, guideOrdinal, guideForm);
  }
}
