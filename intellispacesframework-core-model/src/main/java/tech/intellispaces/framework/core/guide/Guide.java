package tech.intellispaces.framework.core.guide;

import tech.intellispaces.framework.core.exception.TraverseException;

/**
 * Guide.<p/>
 *
 * Guide is a small system designed for processing objects. Guide can map or move objects.
 * Guides splits code into lightweight systems.<p/>
 *
 * Guide can act back on the initiator (other guide) by passing it a backward object.<p/>
 *
 * <p>The guide is object. Guide can be constructed from other guides.
 *
 * @param <S> source object handle type.
 * @param <B> backward object handle type.
 */
public interface Guide<S, B> {

  /**
   * Guide kind.
   */
  GuideKind kind();

  /**
   * Related transition ID.
   */
  String tid();

  /**
   * Synchronous execution of the guide.
   *
   * @param source source object.
   * @param qualifiers guide qualifiers.
   * @return backward object.
   * @throws TraverseException throws if guide was started, but can't traverse source object.
   */
  B traverse(S source, Object... qualifiers) throws TraverseException;
}
