package tech.intellispaces.jaquarius.system;

import tech.intellispaces.jaquarius.guide.Guide;
import tech.intellispaces.statementsj.method.MethodStatement;

/**
 * The system unit guide.
 *
 * @param <S> the source handle type.
 * @param <R> the result handle type.
 */
public interface UnitGuide<S, R> extends Guide<S, R> {

  MethodStatement guideMethod();

  int guideOrdinal();
}
