package tech.intellispaces.reflections.framework.guide.n1;

import tech.intellispaces.core.Rid;
import tech.intellispaces.javareflection.method.MethodStatement;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.system.UnitWrapper;

/**
 * Unit method mover with one qualifier.
 *
 * @param <S> the source reflection type.
 * @param <Q> the qualifier reflection type.
 */
public class UnitMover1<S, Q>
    extends UnitGuide1<S, S, Q>
    implements AbstractMover1<S, Q>
{
  public UnitMover1(
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
