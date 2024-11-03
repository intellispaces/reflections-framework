package intellispaces.jaquarius.guide.n1;

import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.jaquarius.guide.GuideForm;
import intellispaces.jaquarius.system.ObjectHandleWrapper;

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
      int channelIndex,
      GuideForm guideForm
  ) {
    super(cid, objectHandleClass, guideMethod, channelIndex, guideForm);
  }
}
