package tech.intellispaces.reflections.framework.guide.n0;

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
 */
public class ObjectMover0<S extends ReflectionWrapper>
    extends ObjectGuide0<S, S>
    implements AbstractMover0<S>
{
  public ObjectMover0(
      Rid cid,
      Class<S> reflectionClass,
      MethodStatement guideMethod,
      int traverseOrdinal,
      ReflectionForm targetForm
  ) {
    super(cid, reflectionClass, targetForm, guideMethod, traverseOrdinal);
  }
}
