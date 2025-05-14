package tech.intellispaces.reflections.framework.traverse.plan;

import tech.intellispaces.reflections.framework.exception.TraverseException;

/**
 * The plan to traverse source object in semantic space.
 */
public interface TraversePlan {

  /**
   * Traverse plan type.
   */
  TraversePlanType type();

  boolean isDeclarative();

  /**
   * Executes traverse plan to source reflection.
   *
   * @param source the source reflection.
   * @param executor the traverse executor.
   * @return the result reflection.
   * @throws TraverseException throws if source reflection can't be traversed.
   */
  Object execute(Object source, TraverseExecutor executor) throws TraverseException;

  int executeReturnInt(Object source, TraverseExecutor executor) throws TraverseException;

  double executeReturnDouble(Object source, TraverseExecutor executor) throws TraverseException;

  /**
   * Executes traverse plan to source reflection and qualifier.
   *
   * @param source the source reflection.
   * @param qualifier the qualifier.
   * @param executor the traverse executor.
   * @return the result reflection.
   * @throws TraverseException throws if source reflection can't be traversed.
   */
  Object execute(Object source, Object qualifier, TraverseExecutor executor) throws TraverseException;

  /**
   * Executes traverse plan to source reflection and two qualifiers.
   *
   * @param source the source reflection.
   * @param qualifier1 the first qualifier.
   * @param qualifier2 the second qualifier.
   * @param executor the traverse executor.
   * @return the result reflection.
   * @throws TraverseException throws if source reflection can't be traversed.
   */
  Object execute(Object source, Object qualifier1, Object qualifier2, TraverseExecutor executor) throws TraverseException;

  /**
   * Executes traverse plan to source reflection and three qualifiers.
   *
   * @param source the source reflection.
   * @param qualifier1 the first qualifier.
   * @param qualifier2 the second qualifier.
   * @param qualifier3 the third qualifier.
   * @param executor the traverse executor.
   * @return the result reflection.
   * @throws TraverseException throws if source reflection can't be traversed.
   */
  Object execute(Object source, Object qualifier1, Object qualifier2, Object qualifier3, TraverseExecutor executor) throws TraverseException;

  /**
   * Executes traverse plan to source reflection and three qualifiers.
   *
   * @param source the source reflection.
   * @param qualifier1 the first qualifier.
   * @param qualifier2 the second qualifier.
   * @param qualifier3 the third qualifier.
   * @param qualifier4 the four qualifier.
   * @param executor the traverse executor.
   * @return the result reflection.
   * @throws TraverseException throws if source reflection can't be traversed.
   */
  Object execute(Object source, Object qualifier1, Object qualifier2, Object qualifier3, Object qualifier4, TraverseExecutor executor) throws TraverseException;
}
