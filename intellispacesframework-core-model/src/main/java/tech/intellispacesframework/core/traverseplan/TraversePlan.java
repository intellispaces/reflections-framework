package tech.intellispacesframework.core.traverseplan;

import tech.intellispacesframework.core.exception.TraverseException;

/**
 * Prepared plan to traverse (map or move) source object.
 *
 * <p>Traverse plan is instruction how traverse source object inside semantic space.
 */
public interface TraversePlan {

  /**
   * Traverse plan type.
   */
  TraversePlanType type();

  /**
   * Applies this traverse plan to source object.
   *
   * @param source source object handle.
   * @return target object handle.
   * @throws TraverseException throws if source object can't be traversed.
   */
  Object traverse(Object source) throws TraverseException;

  /**
   * Applies this traverse plan to source object and qualifier.
   *
   * @param source source object handle.
   * @param qualifier qualifier.
   * @return target object handle.
   * @throws TraverseException throws if source object can't be traversed.
   */
  Object traverse(Object source, Object qualifier) throws TraverseException;
}
