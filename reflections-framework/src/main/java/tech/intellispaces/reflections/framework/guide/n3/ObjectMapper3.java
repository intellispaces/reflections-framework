package tech.intellispaces.reflections.framework.guide.n3;

import tech.intellispaces.javareflection.method.MethodStatement;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.system.ReflectionWrapper;

/**
 * The reflection implementation mapper guide.<p/>
 *
 * Attached guide can be used exclusively with this reflection implementation only.
 *
 * @param <S> the source reflection type.
 * @param <Q1> the first qualifier reflection type.
 * @param <Q2> the second qualifier reflection type.
 * @param <Q3> the third qualifier reflection type.
 */
public class ObjectMapper3<S extends ReflectionWrapper, T, Q1, Q2, Q3>
    extends ObjectGuide3<S, T, Q1, Q2, Q3>
    implements AbstractMapper3<S, T, Q1, Q2, Q3>
{
  public ObjectMapper3(
      String cid,
      Class<S> reflectionClass,
      MethodStatement guideMethod,
      int traverseOrdinal,
      ReflectionForm targetForm
  ) {
    super(cid, reflectionClass, guideMethod, traverseOrdinal, targetForm);
  }
}
