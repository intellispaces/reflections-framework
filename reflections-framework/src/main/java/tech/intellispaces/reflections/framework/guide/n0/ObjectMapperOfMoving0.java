package tech.intellispaces.reflections.framework.guide.n0;

import tech.intellispaces.jstatements.method.MethodStatement;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.system.ReflectionWrapper;

/**
 * The reflection implementation mapper related to moving.<p/>
 *
 * Attached guide can be used exclusively with this reflection implementation only.
 *
 * @param <S> the source reflection type.
 */
public class ObjectMapperOfMoving0<S extends ReflectionWrapper, T>
    extends ObjectGuide0<S, T>
    implements AbstractMapperOfMoving0<S, T>
{
  public ObjectMapperOfMoving0(
      String cid,
      Class<S> reflectionClass,
      MethodStatement guideMethod,
      int traverseOrdinal,
      ReflectionForm targetForm
  ) {
    super(cid, reflectionClass, targetForm, guideMethod, traverseOrdinal);
  }
}
