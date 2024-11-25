package tech.intellispaces.jaquarius.guide.n1;

import tech.intellispaces.jaquarius.guide.GuideForm;
import tech.intellispaces.jaquarius.system.ObjectHandleWrapper;
import tech.intellispaces.java.reflection.method.MethodStatement;

/**
 * Attached to object handle mover guide.<p/>
 *
 * Attached guide can be used exclusively with this object handle only.
 *
 * @param <S> source object handle type.
 * @param <Q> qualified object handle type.
 */
public class ObjectMover1<S extends ObjectHandleWrapper, Q>
    extends ObjectGuide1<S, S, Q>
    implements AbstractMover1<S, Q>
{
  public ObjectMover1(
      String cid,
      Class<S> objectHandleClass,
      MethodStatement guideMethod,
      int channelIndex,
      GuideForm guideForm
  ) {
    super(cid, objectHandleClass, guideMethod, channelIndex, guideForm);
  }
}
