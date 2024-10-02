package intellispaces.framework.core.guide.n3;

import intellispaces.framework.core.system.ObjectHandleWrapper;

import java.lang.reflect.Method;

/**
 * Attached to object handle mover guide.<p/>
 *
 * Attached guide can be used exclusively with this object handle only.
 *
 * @param <S> source type.
 * @param <Q1> first qualifier type.
 * @param <Q2> second qualifier type.
 */
public class ObjectMover3<S extends ObjectHandleWrapper, Q1, Q2, Q3>
    extends ObjectGuide3<S, S, Q1, Q2, Q3>
    implements AbstractMover3<S, Q1, Q2, Q3>
{
  public ObjectMover3(
      String cid,
      Class<S> objectHandleClass,
      Method guideMethod,
      int channelIndex
  ) {
    super(cid, objectHandleClass, guideMethod, channelIndex);
  }
}
