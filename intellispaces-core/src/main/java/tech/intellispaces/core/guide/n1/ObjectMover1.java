package tech.intellispaces.core.guide.n1;

import tech.intellispaces.commons.exception.UnexpectedViolationException;
import tech.intellispaces.core.exception.TraverseException;
import tech.intellispaces.core.guide.GuideLogger;
import tech.intellispaces.core.object.ObjectHandleWrapper;

import java.lang.reflect.Method;

/**
 * Attached to object handle mover guide.<p/>
 *
 * Attached guide can be used exclusively with this object handle only.
 *
 * @param <S> source object handle type.
 * @param <B> backward object handle type.
 * @param <Q> qualified object handle type.
 */
public class ObjectMover1<S extends ObjectHandleWrapper<S>, B, Q> implements AbstractMover1<S, B, Q> {
  private final Class<S> objectHandleClass;
  private final String tid;
  private final Method guideMethod;
  private final int guideActionIndex;

  public ObjectMover1(
      String tid,
      Class<S> objectHandleClass,
      Method guideMethod,
      int guideActionIndex
  ) {
    if (guideMethod.getParameterCount() != 1) {
      throw UnexpectedViolationException.withMessage("Attached guide should have 1 parameter");
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
  public B move(S source, Q qualifier) throws TraverseException {
    try {
      GuideLogger.logCallGuide(guideMethod);
      return (B) source.getGuideAction(guideActionIndex).asAction1().execute(qualifier);
    } catch (TraverseException e) {
      throw e;
    } catch (Exception e) {
      throw TraverseException.withCauseAndMessage(e, "Failed to invoke guide method {} of object handle {}",
          guideMethod.getName(), objectHandleClass.getCanonicalName());
    }
  }
}
