package tech.intellispaces.jaquarius.guide.n2;

import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForm;
import tech.intellispaces.jaquarius.system.ObjectHandleWrapper;
import tech.intellispaces.java.reflection.method.MethodStatement;

/**
 * Attached to object handle mover guide.<p/>
 *
 * Attached guide can be used exclusively with this object handle only.
 *
 * @param <S> source type.
 * @param <Q1> first qualifier type.
 * @param <Q2> second qualifier type.
 */
public class ObjectMover2<S extends ObjectHandleWrapper, Q1, Q2>
    extends ObjectGuide2<S, S, Q1, Q2>
    implements AbstractMover2<S, Q1, Q2>
{
  public ObjectMover2(
      String cid,
      Class<S> objectHandleClass,
      MethodStatement guideMethod,
      int channelIndex,
      ObjectReferenceForm targetForm
  ) {
    super(cid, objectHandleClass, guideMethod, channelIndex, targetForm);
  }
}
