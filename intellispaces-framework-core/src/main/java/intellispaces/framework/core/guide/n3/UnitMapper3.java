package intellispaces.framework.core.guide.n3;

import intellispaces.framework.core.guide.GuideForm;
import intellispaces.framework.core.system.UnitWrapper;

import java.lang.reflect.Method;

/**
 * Unit method mapper with three qualifier.
 *
 * @param <S> source object handle type.
 * @param <T> target object handle type.
 * @param <Q1> first qualifier object handle type.
 * @param <Q2> second qualifier object handle type.
 * @param <Q3> third  qualifier object handle type.
 */
public class UnitMapper3<S, T, Q1, Q2, Q3>
    extends UnitGuide3<S, T, Q1, Q2, Q3>
    implements AbstractMapper3<S, T, Q1, Q2, Q3>
{
  public UnitMapper3(String cid, UnitWrapper unitInstance, Method guideMethod, int guideOrdinal, GuideForm guideForm) {
    super(cid, unitInstance, guideMethod, guideOrdinal, guideForm);
  }
}
