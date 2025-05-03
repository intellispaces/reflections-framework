package tech.intellispaces.reflections.guide;

import tech.intellispaces.reflections.exception.TraverseException;
import tech.intellispaces.reflections.object.reference.ObjectReferenceForm;

/**
 * The guide.<p/>
 *
 * The guide is a microsystem designed for one specific processing of objects of the same type.
 *
 * Guides splits system into lightweight subsystems.<p/>
 *
 * The guide is object.<p/>
 * Guide can be constructed from other guides.<p/>
 *
 * @param <S> the source handle type. This type defines the guide applicability.
 * @param <R> the result handle type.
 */
public interface Guide<S, R> {

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

  /**
   * Guide form.
   */
  ObjectReferenceForm targetForm();

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
