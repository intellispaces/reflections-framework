package tech.intellispaces.reflectionsframework.system;

import tech.intellispaces.reflectionsframework.guide.Guide;
import tech.intellispaces.jstatements.method.MethodStatement;

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
