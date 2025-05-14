package tech.intellispaces.reflections.framework.guide.n2;

import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.system.ReflectionWrapper;
import tech.intellispaces.jstatements.method.MethodStatement;

/**
 * The reflection implementation mover guide.<p/>
 *
 * Attached guide can be used exclusively with this reflection implementation only.
 *
 * @param <S> the source reflection type.
 * @param <Q1> the first qualifier reflection type.
 * @param <Q2> the second qualifier reflection type.
 */
public class ObjectMover2<S extends ReflectionWrapper, Q1, Q2>
    extends ObjectGuide2<S, S, Q1, Q2>
    implements AbstractMover2<S, Q1, Q2>
{
  public ObjectMover2(
      String cid,
      Class<S> reflectionClass,
      MethodStatement guideMethod,
      int traverseOrdinal,
      ReflectionForm targetForm
  ) {
    super(cid, reflectionClass, guideMethod, traverseOrdinal, targetForm);
  }
}
