package tech.intellispaces.jaquarius.guide;

import tech.intellispaces.jaquarius.exception.TraverseException;
import tech.intellispaces.jaquarius.object.reference.ObjectForm;

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
  String cid();

  /**
   * Guide form.
   */
  ObjectForm targetForm();

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
