package tech.intellispaces.core.guide.n0;

import tech.intellispaces.commons.exception.UnexpectedViolationException;
import tech.intellispaces.core.exception.TraverseException;
import tech.intellispaces.core.object.ObjectHandleWrapper;

import java.lang.reflect.Method;

/**
 * Attached to object handle mover guide.<p/>
 *
 * Attached guide can be used exclusively with this object handle only.
 *
 * @param <S> source object handle type.
 * @param <B> backward object handle type.
 */
public class AttachedMover0<S extends ObjectHandleWrapper<S>, B> implements AbstractMover0<S, B> {
  private final Class<S> objectHandleClass;
  private final String tid;
  private final Method guideMethod;
  private final int guideActionIndex;

  public AttachedMover0(
      String tid,
      Class<S> objectHandleClass,
      Method guideMethod,
      int guideActionIndex
  ) {
    if (guideMethod.getParameterCount() != 0) {
      throw UnexpectedViolationException.withMessage("Attached guide should not have parameters");
    }
    this.tid = tid;
    this.objectHandleClass = objectHandleClass;
    this.guideMethod = guideMethod;
    this.guideActionIndex = guideActionIndex;
  }

  @Override
  public String tid() {
    return tid;
  }

  @Override
  @SuppressWarnings("unchecked")
  public B move(S source) throws TraverseException {
    try {
      return (B) source.getAttachedGuideAction(guideActionIndex).asAction0().execute();
    } catch (TraverseException e) {
      throw e;
    } catch (Exception e) {
      throw TraverseException.withCauseAndMessage(e, "Failed to invoke attached guide method {} of object handle {}",
          guideMethod.getName(), objectHandleClass.getCanonicalName());
    }
  }
}
