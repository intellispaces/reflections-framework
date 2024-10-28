package intellispaces.jaquarius.guide.n0;

import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.jaquarius.guide.GuideForm;
import intellispaces.jaquarius.system.ObjectHandleWrapper;

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
      int channelIndex,
      GuideForm guideForm
  ) {
    super(cid, objectHandleClass, guideForm, guideMethod, channelIndex);
  }
}
