package intellispaces.framework.core.guide.n2;

import intellispaces.framework.core.system.UnitWrapper;

import java.lang.reflect.Method;

public class UnitMapperOfMoving2<S, T, Q1, Q2> extends UnitGuide2<S, T, Q1, Q2> implements AbstractMapperOfMoving2<S, T, Q1, Q2> {

  public UnitMapperOfMoving2(String tid, UnitWrapper unitInstance, Method guideMethod, int guideOrdinal) {
    super(tid, unitInstance, guideMethod, guideOrdinal);
  }
}
