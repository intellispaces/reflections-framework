package intellispaces.framework.core.guide.n1;

import intellispaces.framework.core.system.UnitWrapper;

import java.lang.reflect.Method;

/**
 * Unit method mapper with one qualifier.
 *
 * @param <S> source object handle type.
 * @param <T> target object handle type.
 * @param <Q> qualifier object handle type.
 */
public class UnitMapper1<S, T, Q>
    extends UnitGuide1<S, T, Q>
    implements AbstractMapper1<S, T, Q>
{
  public UnitMapper1(String tid, UnitWrapper unitInstance, Method guideMethod, int guideOrdinal) {
    super(tid, unitInstance, guideMethod, guideOrdinal);
  }
}
