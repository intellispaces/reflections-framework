package tech.intellispaces.jaquarius.guide.n1;

import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForm;
import tech.intellispaces.jaquarius.system.UnitWrapper;
import tech.intellispaces.java.reflection.method.MethodStatement;

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
  public UnitMapper1(
      String cid,
      UnitWrapper unitInstance,
      MethodStatement guideMethod,
      int guideOrdinal,
      ObjectReferenceForm targetForm
  ) {
    super(cid, unitInstance, guideMethod, guideOrdinal, targetForm);
  }
}
