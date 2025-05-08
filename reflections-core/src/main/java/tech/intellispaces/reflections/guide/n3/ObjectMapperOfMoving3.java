package tech.intellispaces.reflections.guide.n3;

import tech.intellispaces.reflections.object.reference.ObjectReferenceForm;
import tech.intellispaces.reflections.system.ObjectHandleWrapper;
import tech.intellispaces.jstatements.method.MethodStatement;

/**
 * Attached to object handle mapper related to moving.<p/>
 *
 * Attached guide can be used exclusively with this object handle only.
 *
 * @param <S> source object type.
 * @param <T> target object type.
 * @param <Q1> first qualifier object type.
 * @param <Q2> second qualifier object type.
 * @param <Q3> third qualifier object type.
 */
public class ObjectMapperOfMoving3<S extends ObjectHandleWrapper, T, Q1, Q2, Q3>
    extends ObjectGuide3<S, T, Q1, Q2, Q3>
    implements AbstractMapperOfMoving3<S, T, Q1, Q2, Q3>
{
  public ObjectMapperOfMoving3(
      String cid,
      Class<S> objectHandleClass,
      MethodStatement guideMethod,
      int traverseOrdinal,
      ObjectReferenceForm targetForm
  ) {
    super(cid, objectHandleClass, guideMethod, traverseOrdinal, targetForm);
  }
}
