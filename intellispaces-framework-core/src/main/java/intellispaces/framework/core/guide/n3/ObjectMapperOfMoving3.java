package intellispaces.framework.core.guide.n3;

import intellispaces.framework.core.guide.GuideForm;
import intellispaces.framework.core.system.ObjectHandleWrapper;

import java.lang.reflect.Method;

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
      Method guideMethod,
      int channelIndex,
      GuideForm guideForm
  ) {
    super(cid, objectHandleClass, guideMethod, channelIndex, guideForm);
  }
}
