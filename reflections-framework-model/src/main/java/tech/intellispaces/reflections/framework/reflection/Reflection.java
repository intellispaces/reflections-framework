package tech.intellispaces.reflections.framework.reflection;

import java.util.List;

/**
 * The focused reflection.
 *
 * @param <D> the domain type.
 */
public interface Reflection<D> extends TypedReflection<D> {

//  <R extends Reflection<D>> R as(Class<R> reflectionClass);

  /**
   * A flag indicating whether a reflection is movable or not.
   *
   * @return <code>true</code> if this reflection is movable or <code>false</code> otherwise.
   */
  boolean isMovable();

  /**
   * Returns movable representation of this reflection throws exception.
   */
  MovableReflection<D> asMovableOrElseThrow();

  /**
   * Sets projection.
   *
   * @param targetDomain the target domain class.
   * @param target the target reflection.
   * @param <TD> the target domain type.
   * @param <T> the target reflection type.
   */
  <TD, T> void addProjection(Class<TD> targetDomain, T target);

  /**
   * Returns nearest known underlying reflection or <code>null</code> if its are unknown.
   */
  List<? extends Reflection<?>> underlyingReflections();

  /**
   * Returns nearest known overlying reflection or <code>null</code> if it is unknown.
   */
  Reflection<?> overlyingReflection();
}
