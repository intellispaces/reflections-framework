package intellispaces.core.traverse;

import intellispaces.core.exception.TraverseException;

/**
 * Plan to traverse (map or move) source object in semantic space.
 */
public interface TraversePlan {

  /**
   * Traverse plan type.
   */
  TraversePlanType type();

  boolean isDeclarative();

  /**
   * Executes traverse plan to source object.
   *
   * @param source source object handle.
   * @param executor traverse executor.
   * @return backward object handle.
   * @throws TraverseException throws if source object can't be traversed.
   */
  Object execute(Object source, TraverseExecutor executor) throws TraverseException;

  /**
   * Executes traverse plan to source object and qualifier.
   *
   * @param source source object handle.
   * @param qualifier qualifier.
   * @param executor traverse executor.
   * @return backward object handle.
   * @throws TraverseException throws if source object can't be traversed.
   */
  Object execute(Object source, Object qualifier, TraverseExecutor executor) throws TraverseException;

  /**
   * Executes traverse plan to source object and qualifier.
   *
   * @param source source object handle.
   * @param qualifier1 first qualifier.
   * @param qualifier2 second qualifier.
   * @param executor traverse executor.
   * @return backward object handle.
   * @throws TraverseException throws if source object can't be traversed.
   */
  Object execute(Object source, Object qualifier1, Object qualifier2, TraverseExecutor executor) throws TraverseException;
}
