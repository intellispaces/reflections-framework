package intellispaces.framework.core.guide.n1;

import intellispaces.framework.core.system.UnitWrapper;

import java.lang.reflect.Method;

public class UnitMapperOfMoving1<S, T, Q> extends UnitGuide1<S, T, Q> implements AbstractMapperOfMoving1<S, T, Q> {

  public UnitMapperOfMoving1(String cid, UnitWrapper unitInstance, Method guideMethod, int guideOrdinal) {
    super(cid, unitInstance, guideMethod, guideOrdinal);
  }
}
