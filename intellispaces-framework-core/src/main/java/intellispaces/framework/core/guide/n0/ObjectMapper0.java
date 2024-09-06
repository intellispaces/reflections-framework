package intellispaces.framework.core.guide.n0;

import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.framework.core.exception.TraverseException;
import intellispaces.framework.core.guide.GuideLogger;
import intellispaces.framework.core.system.ObjectHandleWrapper;

import java.lang.reflect.Method;

/**
 * Attached to object handle mapper guide.<p/>
 *
 * Attached guide can be used exclusively with this object handle only.
 *
 * @param <S> mapper source object type.
 */
public class ObjectMapper0<S extends ObjectHandleWrapper<S>, T> implements AbstractMapper0<S, T> {
  private final Class<S> objectHandleClass;
  private final String tid;
  private final Method guideMethod;
  private final int transitionIndex;

  public ObjectMapper0(
      String tid,
      Class<S> objectHandleClass,
      Method guideMethod,
      int transitionIndex
  ) {
    if (guideMethod.getParameterCount() != 0) {
      throw UnexpectedViolationException.withMessage("Guide should not have parameters");
    }
    this.tid = tid;
    this.objectHandleClass = objectHandleClass;
    this.guideMethod = guideMethod;
    this.transitionIndex = transitionIndex;
  }

  @Override
  public String tid() {
    return tid;
  }

  @Override
  @SuppressWarnings("unchecked")
  public T map(S source) throws TraverseException {
    try {
      GuideLogger.logCallGuide(guideMethod);
      return (T) source.$handle().getGuideAction(transitionIndex).asAction0().execute();
    } catch (TraverseException e) {
      throw e;
    } catch (Exception e) {
      throw TraverseException.withCauseAndMessage(e, "Failed to invoke object method ''{0}'' of object handle {1}",
          guideMethod.getName(), objectHandleClass.getCanonicalName());
    }
  }
}
