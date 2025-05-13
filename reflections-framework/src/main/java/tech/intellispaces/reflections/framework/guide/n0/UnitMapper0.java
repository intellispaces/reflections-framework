package tech.intellispaces.reflections.framework.guide.n0;

import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.system.UnitWrapper;
import tech.intellispaces.jstatements.method.MethodStatement;

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
      ReflectionForm targetForm
  ) {
    super(cid, unitInstance, guideMethod, guideOrdinal, targetForm);
  }
}
