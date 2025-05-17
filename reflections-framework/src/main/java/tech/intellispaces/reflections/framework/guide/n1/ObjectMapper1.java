package tech.intellispaces.reflections.framework.guide.n1;

import tech.intellispaces.jstatements.method.MethodStatement;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.system.ReflectionWrapper;

/**
 * The reflection implementation mapper guide.<p/>
 *
 * Attached guide can be used exclusively with this reflection implementation only.
 *
 * @param <S> the source reflection type.
 * @param <Q> the qualifier reflection type.
 */
public class ObjectMapper1<S extends ReflectionWrapper, T, Q>
    extends ObjectGuide1<S, T, Q>
    implements AbstractMapper1<S, T, Q>
{
  public ObjectMapper1(
      String cid,
      Class<S> reflectionClass,
      MethodStatement guideMethod,
      int traverseOrdinal,
      ReflectionForm targetForm
  ) {
    super(cid, reflectionClass, guideMethod, traverseOrdinal, targetForm);
  }
}
