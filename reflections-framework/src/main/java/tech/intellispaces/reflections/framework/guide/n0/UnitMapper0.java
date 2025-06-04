package tech.intellispaces.reflections.framework.guide.n0;

import tech.intellispaces.core.Rid;
import tech.intellispaces.javareflection.method.MethodStatement;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.system.UnitWrapper;

/**
 * Non-parameterized unit method mapper.
 *
 * @param <S> the source reflection type.
 */
public class UnitMapper0<S, T> extends UnitGuide0<S, T> implements AbstractMapper0<S, T> {

  public UnitMapper0(
      Rid cid,
      UnitWrapper unitInstance,
      MethodStatement guideMethod,
      int guideOrdinal,
      Class<S> sourceClass,
      ReflectionForm targetForm
  ) {
    super(cid, unitInstance, guideMethod, guideOrdinal, sourceClass, targetForm);
  }
}
