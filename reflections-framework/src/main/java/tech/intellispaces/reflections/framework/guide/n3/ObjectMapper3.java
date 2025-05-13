package tech.intellispaces.reflections.framework.guide.n3;

import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.system.ObjectHandleWrapper;
import tech.intellispaces.jstatements.method.MethodStatement;

/**
 * Attached to object handle mapper guide.<p/>
 *
 * Attached guide can be used exclusively with this object handle only.
 *
 * @param <S> source object type.
 * @param <Q1> first qualifier type.
 * @param <Q2> second qualifier type.
 * @param <Q3> third qualifier type.
 */
public class ObjectMapper3<S extends ObjectHandleWrapper, T, Q1, Q2, Q3>
    extends ObjectGuide3<S, T, Q1, Q2, Q3>
    implements AbstractMapper3<S, T, Q1, Q2, Q3>
{
  public ObjectMapper3(
      String cid,
      Class<S> objectHandleClass,
      MethodStatement guideMethod,
      int traverseOrdinal,
      ReflectionForm targetForm
  ) {
    super(cid, objectHandleClass, guideMethod, traverseOrdinal, targetForm);
  }
}
