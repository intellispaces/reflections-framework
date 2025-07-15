package tech.intellispaces.reflections.framework.guide;

import tech.intellispaces.actions.Action;
import tech.intellispaces.core.Guide;
import tech.intellispaces.core.Rid;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.guide.n0.SystemGuide0;
import tech.intellispaces.reflections.framework.guide.n1.SystemGuide1;
import tech.intellispaces.reflections.framework.guide.n2.SystemGuide2;
import tech.intellispaces.reflections.framework.guide.n3.SystemGuide3;
import tech.intellispaces.reflections.framework.guide.n4.SystemGuide4;
import tech.intellispaces.reflections.framework.guide.n5.SystemGuide5;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;

/**
 * The system guide.
 * <p>
 * Guides splits system into lightweight parts.
 * <p>
 * The guide is object. Guide can be constructed from other guides.
 *
 * @param <S> the source reflection type. This type defines the guide applicability.
 * @param <R> the result reflection type.
 */
public interface SystemGuide<S, R> extends Guide {

  /**
   * The guide kind.
   */
  GuideKind kind();

  /**
   * The related channel ID.<p/>
   *
   * Related channel defined guide capability.
   */
  Rid channelId();

  /**
   * Returns source reflection class.
   */
  Class<S> sourceClass();

  /**
   * The target reflection form.
   */
  ReflectionForm targetForm();

  /**
   * Synchronous execution of the guide.
   *
   * @param source source object.
   * @param qualifiers guide qualifiers.
   * @return returned object.
   * @throws TraverseException throws if guide was started, but can't traverse source object.
   */
  R traverse(S source, Object[] qualifiers) throws TraverseException;

  /**
   * Casts this guide to level 0 form or throw exception.
   */
  SystemGuide0<S, R> asGuide0();

  /**
   * Casts this guide to level 1 form or throw exception.
   */
  <Q> SystemGuide1<S, R, Q> asGuide1();

  /**
   * Casts this guide to level 2 form or throw exception.
   */
  <Q1, Q2> SystemGuide2<S, R, Q1, Q2> asGuide2();

  /**
   * Casts this guide to level 3 form or throw exception.
   */
  <Q1, Q2, Q3> SystemGuide3<S, R, Q1, Q2, Q3> asGuide3();

  /**
   * Casts this guide to level 4 form or throw exception.
   */
  <Q1, Q2, Q3, Q4> SystemGuide4<S, R, Q1, Q2, Q3, Q4> asGuide4();

  /**
   * Casts this guide to level 5 form or throw exception.
   */
  <Q1, Q2, Q3, Q4, Q5> SystemGuide5<S, R, Q1, Q2, Q3, Q4, Q5> asGuide5();
}
