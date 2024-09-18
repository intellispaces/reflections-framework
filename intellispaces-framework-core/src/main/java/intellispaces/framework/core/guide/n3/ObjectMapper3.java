package intellispaces.framework.core.guide.n3;

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
 * @param <S> source object type.
 * @param <Q1> first qualifier type.
 * @param <Q2> second qualifier type.
 * @param <Q3> third qualifier type.
 */
public class ObjectMapper3<S extends ObjectHandleWrapper<S>, T, Q1, Q2, Q3> implements AbstractMapper3<S, T, Q1, Q2, Q3> {
  private final Class<S> objectHandleClass;
  private final String tid;
  private final Method guideMethod;
  private final int transitionIndex;

  public ObjectMapper3(
      String tid,
      Class<S> objectHandleClass,
      Method guideMethod,
      int transitionIndex
  ) {
    if (guideMethod.getParameterCount() != 3) {
      throw UnexpectedViolationException.withMessage("Guide should have three qualifiers");
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
  public T map(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3) throws TraverseException {
    try {
      GuideLogger.logCallGuide(guideMethod);
      return (T) source.$handle().getGuideAction(transitionIndex).asAction3().execute(qualifier1, qualifier2, qualifier3);
    } catch (TraverseException e) {
      throw e;
    } catch (Exception e) {
      throw TraverseException.withCauseAndMessage(e, "Failed to invoke guide method ''{0}'' of object handle {1}",
          guideMethod.getName(), objectHandleClass.getCanonicalName());
    }
  }
}
