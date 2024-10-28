package intellispaces.jaquarius.guide.n1;

import intellispaces.jaquarius.guide.GuideForm;
import intellispaces.jaquarius.system.UnitWrapper;

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
  public UnitMapper1(String cid, UnitWrapper unitInstance, Method guideMethod, int guideOrdinal, GuideForm guideForm) {
    super(cid, unitInstance, guideMethod, guideOrdinal, guideForm);
  }
}
