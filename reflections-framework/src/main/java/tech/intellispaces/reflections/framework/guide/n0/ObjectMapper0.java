package tech.intellispaces.reflections.framework.guide.n0;

import tech.intellispaces.core.Rid;
import tech.intellispaces.javareflection.method.MethodStatement;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.system.ReflectionWrapper;

/**
 * The reflection implementation mapper guide.<p/>
 *
 * Attached guide can be used exclusively with this reflection implementation only.
 *
 * @param <S> the source reflection type.
 */
public class ObjectMapper0<S extends ReflectionWrapper, T>
    extends ObjectGuide0<S, T>
    implements AbstractMapper0<S, T>
{
  public ObjectMapper0(
      Rid cid,
      Class<S> reflectionClass,
      MethodStatement guideMethod,
      int traverseOrdinal,
      ReflectionForm targetForm
  ) {
    super(cid, reflectionClass, targetForm, guideMethod, traverseOrdinal);
  }
}
