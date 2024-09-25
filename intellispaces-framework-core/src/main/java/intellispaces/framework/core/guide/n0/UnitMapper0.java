package intellispaces.framework.core.guide.n0;

import intellispaces.framework.core.system.UnitWrapper;

import java.lang.reflect.Method;

/**
 * Non-parameterized unit method mapper.
 *
 * @param <S> source object handle type.
 */
public class UnitMapper0<S, T> extends UnitGuide0<S, T> implements AbstractMapper0<S, T> {

  public UnitMapper0(String tid, UnitWrapper unitInstance, Method guideMethod, int guideOrdinal) {
    super(tid, unitInstance, guideMethod, guideOrdinal);
  }
}
