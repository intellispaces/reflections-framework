package tech.intellispaces.reflectionsframework.guide.n0;

import tech.intellispaces.reflectionsframework.object.reference.ObjectReferenceForm;
import tech.intellispaces.reflectionsframework.system.ObjectHandleWrapper;
import tech.intellispaces.jstatements.method.MethodStatement;

/**
 * Attached to object handle mover guide.<p/>
 *
 * Attached guide can be used exclusively with this object handle only.
 *
 * @param <S> source object handle type.
 */
public class ObjectMover0<S extends ObjectHandleWrapper>
    extends ObjectGuide0<S, S>
    implements AbstractMover0<S>
{
  public ObjectMover0(
      String cid,
      Class<S> objectHandleClass,
      MethodStatement guideMethod,
      int traverseOrdinal,
      ObjectReferenceForm targetForm
  ) {
    super(cid, objectHandleClass, targetForm, guideMethod, traverseOrdinal);
  }
}
