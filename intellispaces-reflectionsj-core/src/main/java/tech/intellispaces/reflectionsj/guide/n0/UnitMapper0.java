package tech.intellispaces.reflectionsj.guide.n0;

import tech.intellispaces.reflectionsj.object.reference.ObjectReferenceForm;
import tech.intellispaces.reflectionsj.system.UnitWrapper;
import tech.intellispaces.statementsj.method.MethodStatement;

/**
 * Non-parameterized unit method mapper.
 *
 * @param <S> source object handle type.
 */
public class UnitMapper0<S, T> extends UnitGuide0<S, T> implements AbstractMapper0<S, T> {

  public UnitMapper0(
      String cid,
      UnitWrapper unitInstance,
      MethodStatement guideMethod,
      int guideOrdinal,
      ObjectReferenceForm targetForm
  ) {
    super(cid, unitInstance, guideMethod, guideOrdinal, targetForm);
  }
}
