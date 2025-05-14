package tech.intellispaces.reflections.framework.guide.n1;

import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.system.ReflectionWrapper;
import tech.intellispaces.jstatements.method.MethodStatement;

/**
 * The reflection implementation mapper related to moving.<p/>
 *
 * Attached guide can be used exclusively with this reflection implementation only.
 *
 * @param <S> the source reflection type.
 * @param <T> the target reflection type.
 * @param <Q> the qualifier reflection type.
 */
public class ObjectMapperOfMoving1<S extends ReflectionWrapper, T, Q>
    extends ObjectGuide1<S, T, Q>
    implements AbstractMapperOfMoving1<S, T, Q>
{
  public ObjectMapperOfMoving1(
      String cid,
      Class<S> reflectionClass,
      MethodStatement guideMethod,
      int traverseOrdinal,
      ReflectionForm targetForm
  ) {
    super(cid, reflectionClass, guideMethod, traverseOrdinal, targetForm);
  }
}
