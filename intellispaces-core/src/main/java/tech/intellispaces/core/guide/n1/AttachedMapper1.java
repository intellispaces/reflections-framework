package tech.intellispaces.core.guide.n1;

import tech.intellispaces.commons.exception.UnexpectedViolationException;
import tech.intellispaces.core.exception.TraverseException;
import tech.intellispaces.core.object.ObjectHandleWrapper;

import java.lang.reflect.Method;

/**
 * Attached to object handle mapper guide.<p/>
 *
 * Attached guide can be used exclusively with this object handle only.
 *
 * @param <S> mapper source object type.
 * @param <Q> mapper qualified type.
 */
public class AttachedMapper1<S extends ObjectHandleWrapper<S>, T, Q> implements AbstractMapper1<S, T, Q> {
  private final Class<S> objectHandleClass;
  private final String tid;
  private final Method guideMethod;
  private final int guideActionIndex;

  public AttachedMapper1(
      String tid,
      Class<S> objectHandleClass,
      Method guideMethod,
      int guideActionIndex
  ) {
    if (guideMethod.getParameterCount() != 1) {
      throw UnexpectedViolationException.withMessage("Guide should have 1 parameter");
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
  public T map(S source, Q qualifier) throws TraverseException {
    try {
      return (T) source.getAttachedGuideAction(guideActionIndex).asAction1().execute(qualifier);
    } catch (TraverseException e) {
      throw e;
    } catch (Exception e) {
      throw TraverseException.withCauseAndMessage(e, "Failed to invoke attached guide method {} of object handle {}",
          guideMethod.getName(), objectHandleClass.getCanonicalName());
    }
  }
}
