package tech.intellispaces.jaquarius.system;

import tech.intellispaces.commons.java.reflection.method.MethodStatement;
import tech.intellispaces.jaquarius.guide.Guide;

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
