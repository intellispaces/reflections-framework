package intellispaces.jaquarius.guide;

import intellispaces.jaquarius.exception.TraverseException;

/**
 * Guide.<p/>
 *
 * Guide is a micro system designed for processing objects. Guide can map or move objects.
 * Guides splits code into lightweight systems.<p/>
 *
 * The guide is object. Guide can be constructed from other guides.<p/>
 *
 * @param <S> source handle type. This type defines guide applicability.
 * @param <R> result handle type.
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
  String cid();

  /**
   * Guide form.
   */
  GuideForm guideForm();

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
