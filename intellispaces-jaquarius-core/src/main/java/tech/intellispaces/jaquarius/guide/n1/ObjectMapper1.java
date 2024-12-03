package tech.intellispaces.jaquarius.guide.n1;

import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForm;
import tech.intellispaces.jaquarius.system.ObjectHandleWrapper;
import tech.intellispaces.java.reflection.method.MethodStatement;

/**
 * Attached to object handle mapper guide.<p/>
 *
 * Attached guide can be used exclusively with this object handle only.
 *
 * @param <S> mapper source object type.
 * @param <Q> mapper qualified type.
 */
public class ObjectMapper1<S extends ObjectHandleWrapper, T, Q>
    extends ObjectGuide1<S, T, Q>
    implements AbstractMapper1<S, T, Q>
{
  public ObjectMapper1(
      String cid,
      Class<S> objectHandleClass,
      MethodStatement guideMethod,
      int traverseOrdinal,
      ObjectReferenceForm targetForm
  ) {
    super(cid, objectHandleClass, guideMethod, traverseOrdinal, targetForm);
  }
}
