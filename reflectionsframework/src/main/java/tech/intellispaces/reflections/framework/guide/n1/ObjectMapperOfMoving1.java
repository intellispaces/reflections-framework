package tech.intellispaces.reflections.framework.guide.n1;

import tech.intellispaces.reflections.framework.object.reference.ObjectReferenceForm;
import tech.intellispaces.reflections.framework.system.ObjectHandleWrapper;
import tech.intellispaces.jstatements.method.MethodStatement;

/**
 * Attached to object handle mapper related to moving.<p/>
 *
 * Attached guide can be used exclusively with this object handle only.
 *
 * @param <S> source object type.
 * @param <T> target object type.
 * @param <Q> qualifier object type.
 */
public class ObjectMapperOfMoving1<S extends ObjectHandleWrapper, T, Q>
    extends ObjectGuide1<S, T, Q>
    implements AbstractMapperOfMoving1<S, T, Q>
{
  public ObjectMapperOfMoving1(
      String cid,
      Class<S> objectHandleClass,
      MethodStatement guideMethod,
      int traverseOrdinal,
      ObjectReferenceForm targetForm
  ) {
    super(cid, objectHandleClass, guideMethod, traverseOrdinal, targetForm);
  }
}
