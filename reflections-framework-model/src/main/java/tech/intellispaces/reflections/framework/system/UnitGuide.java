package tech.intellispaces.reflections.framework.system;

import tech.intellispaces.actions.Action;
import tech.intellispaces.javareflection.method.MethodStatement;
import tech.intellispaces.reflections.framework.guide.SystemGuide;

/**
 * The unit guide.
 *
 * @param <S> the source reflection type.
 * @param <R> the result reflection type.
 */
public interface UnitGuide<S, R> extends SystemGuide<S, R> {

  MethodStatement guideMethod();

  int guideOrdinal();

  /**
   * Returns an action corresponding to this guide.
   */
  Action asAction();
}
