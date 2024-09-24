package intellispaces.framework.core.guide.n3;

import intellispaces.framework.core.system.UnitWrapper;

import java.lang.reflect.Method;

public class UnitMapperOfMoving3<S, T, Q1, Q2, Q3> extends UnitGuide3<S, T, Q1, Q2, Q3> implements AbstractMapperOfMoving3<S, T, Q1, Q2, Q3> {

  public UnitMapperOfMoving3(String tid, UnitWrapper unitInstance, Method guideMethod, int guideActionIndex) {
    super(tid, unitInstance, guideMethod, guideActionIndex);
  }
}
