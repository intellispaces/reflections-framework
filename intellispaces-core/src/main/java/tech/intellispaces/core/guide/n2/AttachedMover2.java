package tech.intellispaces.core.guide.n2;

import tech.intellispaces.commons.exception.UnexpectedViolationException;
import tech.intellispaces.core.exception.TraverseException;
import tech.intellispaces.core.object.ObjectHandleWrapper;

import java.lang.reflect.Method;

/**
 * Attached to object handle mover guide.<p/>
 *
 * Attached guide can be used exclusively with this object handle only.
 *
 * @param <S> source type.
 * @param <B> backward object type.
 * @param <Q1> first qualifier type.
 * @param <Q2> second qualifier type.
 */
public class AttachedMover2<S extends ObjectHandleWrapper<S>, B, Q1, Q2> implements AbstractMover2<S, B, Q1, Q2> {
  private final Class<S> objectHandleClass;
  private final String tid;
  private final Method guideMethod;
  private final int guideActionIndex;

  public AttachedMover2(
      String tid,
      Class<S> objectHandleClass,
      Method guideMethod,
      int guideActionIndex
  ) {
    if (guideMethod.getParameterCount() != 2) {
      throw UnexpectedViolationException.withMessage("Attached guide should have 2 qualifiers");
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
  public B move(S source, Q1 qualifier1, Q2 qualifier2) throws TraverseException {
    try {
      return (B) source.getAttachedGuideAction(guideActionIndex).asAction2().execute(qualifier1, qualifier2);
    } catch (TraverseException e) {
      throw e;
    } catch (Exception e) {
      throw TraverseException.withCauseAndMessage(e, "Failed to invoke attached guide method {} of object handle {}",
          guideMethod.getName(), objectHandleClass.getCanonicalName());
    }
  }
}
