package tech.intellispaces.reflections.framework.guide;

import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;

/**
 * The guide.<p/>
 *
 * The guide is a microsystem designed for one specific processing of objects of the same type.
 *
 * Guides splits system into lightweight parts.<p/>
 *
 * The guide is object.<p/>
 * Guide can be constructed from other guides.<p/>
 *
 * @param <S> the source reflection type. This type defines the guide applicability.
 * @param <R> the result reflection type.
 */
public interface Guide<S, R> extends tech.intellispaces.core.Guide {

  /**
   * Guide kind.
   */
  GuideKind kind();

  /**
   * Related channel ID.<p/>
   *
   * Related channel defined guide capability.
   */
  String channelId();

  Class<S> sourceClass();

  /**
   * Guide form.
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
  R traverse(S source, Object... qualifiers) throws TraverseException;
}
