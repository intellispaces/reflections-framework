package tech.intellispaces.reflections.framework.guide.n1;

import tech.intellispaces.core.Rid;
import tech.intellispaces.javareflection.method.MethodStatement;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.system.ReflectionWrapper;

/**
 * The reflection implementation mover guide.<p/>
 *
 * Attached guide can be used exclusively with this reflection implementation only.
 *
 * @param <S> the source reflection type.
 * @param <Q> the qualified reflection type.
 */
public class ObjectMover1<S extends ReflectionWrapper, Q>
    extends ObjectGuide1<S, S, Q>
    implements AbstractMover1<S, Q>
{
  public ObjectMover1(
      Rid cid,
      Class<S> reflectionClass,
      MethodStatement guideMethod,
      int traverseOrdinal,
      ReflectionForm targetForm
  ) {
    super(cid, reflectionClass, guideMethod, traverseOrdinal, targetForm);
  }
}
