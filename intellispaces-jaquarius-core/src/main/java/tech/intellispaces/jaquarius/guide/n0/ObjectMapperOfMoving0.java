package tech.intellispaces.jaquarius.guide.n0;

import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForm;
import tech.intellispaces.jaquarius.system.ObjectHandleWrapper;
import tech.intellispaces.reflection.method.MethodStatement;

/**
 * Attached to object handle mapper related to moving.<p/>
 *
 * Attached guide can be used exclusively with this object handle only.
 *
 * @param <S> mapper source object type.
 */
public class ObjectMapperOfMoving0<S extends ObjectHandleWrapper, T>
    extends ObjectGuide0<S, T>
    implements AbstractMapperOfMoving0<S, T>
{
  public ObjectMapperOfMoving0(
      String cid,
      Class<S> objectHandleClass,
      MethodStatement guideMethod,
      int traverseOrdinal,
      ObjectReferenceForm targetForm
  ) {
    super(cid, objectHandleClass, targetForm, guideMethod, traverseOrdinal);
  }
}
