package tech.intellispaces.reflections.framework.system;

import tech.intellispaces.jstatements.method.MethodStatement;
import tech.intellispaces.reflections.framework.guide.Guide;

/**
 * The system unit guide.
 *
 * @param <S> the source reflection type.
 * @param <R> the result reflection type.
 */
public interface UnitGuide<S, R> extends Guide<S, R> {

  MethodStatement guideMethod();

  int guideOrdinal();
}
