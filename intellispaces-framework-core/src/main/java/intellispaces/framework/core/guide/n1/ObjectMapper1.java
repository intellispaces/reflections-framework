package intellispaces.framework.core.guide.n1;

import intellispaces.framework.core.system.ObjectHandleWrapper;

import java.lang.reflect.Method;

/**
 * Attached to object handle mapper guide.<p/>
 *
 * Attached guide can be used exclusively with this object handle only.
 *
 * @param <S> mapper source object type.
 * @param <Q> mapper qualified type.
 */
public class ObjectMapper1<S extends ObjectHandleWrapper<S>, T, Q>
    extends ObjectGuide1<S, T, Q>
    implements AbstractMapper1<S, T, Q>
{
  public ObjectMapper1(
      String tid,
      Class<S> objectHandleClass,
      Method guideMethod,
      int transitionIndex
  ) {
    super(tid, objectHandleClass, guideMethod, transitionIndex);
  }
}
