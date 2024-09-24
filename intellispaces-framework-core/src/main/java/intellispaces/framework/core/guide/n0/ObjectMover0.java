package intellispaces.framework.core.guide.n0;

import intellispaces.framework.core.system.ObjectHandleWrapper;

import java.lang.reflect.Method;

/**
 * Attached to object handle mover guide.<p/>
 *
 * Attached guide can be used exclusively with this object handle only.
 *
 * @param <S> source object handle type.
 */
public class ObjectMover0<S extends ObjectHandleWrapper<S>>
    extends ObjectGuide0<S, S>
    implements AbstractMover0<S>
{
  public ObjectMover0(
      String tid,
      Class<S> objectHandleClass,
      Method guideMethod,
      int transitionIndex
  ) {
    super(tid, objectHandleClass, guideMethod, transitionIndex);
  }
}
