package intellispaces.framework.core.guide.n2;

import intellispaces.framework.core.system.ObjectHandleWrapper;

import java.lang.reflect.Method;

/**
 * Attached to object handle mapper guide.<p/>
 *
 * Attached guide can be used exclusively with this object handle only.
 *
 * @param <S> source object type.
 * @param <Q1> first qualifier type.
 * @param <Q2> second qualifier type.
 */
public class ObjectMapper2<S extends ObjectHandleWrapper, T, Q1, Q2>
    extends ObjectGuide2<S, T, Q1, Q2>
    implements AbstractMapper2<S, T, Q1, Q2>
{
  public ObjectMapper2(
      String cid,
      Class<S> objectHandleClass,
      Method guideMethod,
      int channelIndex
  ) {
    super(cid, objectHandleClass, guideMethod, channelIndex);
  }
}
