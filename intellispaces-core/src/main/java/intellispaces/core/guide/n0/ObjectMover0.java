package intellispaces.core.guide.n0;

import intellispaces.commons.exception.UnexpectedViolationException;
import intellispaces.core.exception.TraverseException;
import intellispaces.core.guide.GuideLogger;
import intellispaces.core.system.ObjectHandleWrapper;

import java.lang.reflect.Method;

/**
 * Attached to object handle mover guide.<p/>
 *
 * Attached guide can be used exclusively with this object handle only.
 *
 * @param <S> source object handle type.
 * @param <B> backward object handle type.
 */
public class ObjectMover0<S extends ObjectHandleWrapper<S>, B> implements AbstractMover0<S, B> {
  private final Class<S> objectHandleClass;
  private final String tid;
  private final Method guideMethod;
  private final int transitionIndex;

  public ObjectMover0(
      String tid,
      Class<S> objectHandleClass,
      Method guideMethod,
      int transitionIndex
  ) {
    if (guideMethod.getParameterCount() != 0) {
      throw UnexpectedViolationException.withMessage("Object guide should not have parameters");
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
  public B move(S source) throws TraverseException {
    try {
      GuideLogger.logCallGuide(guideMethod);
      return (B) source.$shadowHandle().getGuideAction(transitionIndex).asAction0().execute();
    } catch (TraverseException e) {
      throw e;
    } catch (Exception e) {
      throw TraverseException.withCauseAndMessage(e, "Failed to invoke guide method {} of object handle {}",
          guideMethod.getName(), objectHandleClass.getCanonicalName());
    }
  }
}
