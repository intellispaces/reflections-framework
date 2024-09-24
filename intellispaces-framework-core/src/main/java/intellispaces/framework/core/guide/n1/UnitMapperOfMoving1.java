package intellispaces.framework.core.guide.n1;

import intellispaces.framework.core.system.UnitWrapper;

import java.lang.reflect.Method;

public class UnitMapperOfMoving1<S, T, Q> extends UnitGuide1<S, T, Q> implements AbstractMapperOfMoving1<S, T, Q> {

  public UnitMapperOfMoving1(String tid, UnitWrapper unitInstance, Method guideMethod, int guideActionIndex) {
    super(tid, unitInstance, guideMethod, guideActionIndex);
  }
}
