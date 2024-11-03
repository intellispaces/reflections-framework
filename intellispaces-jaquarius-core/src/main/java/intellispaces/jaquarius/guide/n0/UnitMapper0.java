package intellispaces.jaquarius.guide.n0;

import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.jaquarius.guide.GuideForm;
import intellispaces.jaquarius.system.UnitWrapper;

/**
 * Non-parameterized unit method mapper.
 *
 * @param <S> source object handle type.
 */
public class UnitMapper0<S, T> extends UnitGuide0<S, T> implements AbstractMapper0<S, T> {

  public UnitMapper0(String cid, UnitWrapper unitInstance, MethodStatement guideMethod, int guideOrdinal, GuideForm guideForm) {
    super(cid, unitInstance, guideMethod, guideOrdinal, guideForm);
  }
}
