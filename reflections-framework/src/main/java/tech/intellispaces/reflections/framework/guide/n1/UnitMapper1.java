package tech.intellispaces.reflections.framework.guide.n1;

import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.system.UnitWrapper;
import tech.intellispaces.jstatements.method.MethodStatement;

/**
 * Unit method mapper with one qualifier.
 *
 * @param <S> the source reflection type.
 * @param <T> the target reflection type.
 * @param <Q> the qualifier reflection type.
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
      ReflectionForm targetForm
  ) {
    super(cid, unitInstance, guideMethod, guideOrdinal, targetForm);
  }
}
