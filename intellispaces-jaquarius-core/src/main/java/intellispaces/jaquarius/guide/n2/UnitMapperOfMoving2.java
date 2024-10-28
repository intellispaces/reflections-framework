package intellispaces.jaquarius.guide.n2;

import intellispaces.jaquarius.guide.GuideForm;
import intellispaces.jaquarius.system.UnitWrapper;

import java.lang.reflect.Method;

public class UnitMapperOfMoving2<S, T, Q1, Q2> extends UnitGuide2<S, T, Q1, Q2> implements AbstractMapperOfMoving2<S, T, Q1, Q2> {

  public UnitMapperOfMoving2(String cid, UnitWrapper unitInstance, Method guideMethod, int guideOrdinal, GuideForm guideForm) {
    super(cid, unitInstance, guideMethod, guideOrdinal, guideForm);
  }
}
