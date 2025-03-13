package tech.intellispaces.jaquarius.guide.n2;

import tech.intellispaces.commons.reflection.method.MethodStatement;
import tech.intellispaces.jaquarius.object.reference.ObjectForm;
import tech.intellispaces.jaquarius.system.UnitWrapper;

/**
 * Unit method mapper with two qualifier.
 *
 * @param <S> source object handle type.
 * @param <T> target object handle type.
 * @param <Q1> first qualifier object handle type.
 * @param <Q2> second qualifier object handle type.
 */
public class UnitMapper2<S, T, Q1, Q2>
    extends UnitGuide2<S, T, Q1, Q2>
    implements AbstractMapper2<S, T, Q1, Q2>
{
  public UnitMapper2(
      String cid,
      UnitWrapper unitInstance,
      MethodStatement guideMethod,
      int guideOrdinal,
      ObjectForm targetForm
  ) {
    super(cid, unitInstance, guideMethod, guideOrdinal, targetForm);
  }
}
