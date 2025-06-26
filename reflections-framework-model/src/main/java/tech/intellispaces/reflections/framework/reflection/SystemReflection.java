package tech.intellispaces.reflections.framework.reflection;

import java.util.List;

import tech.intellispaces.core.Reflection;
import tech.intellispaces.reflections.framework.channel.Channel1;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.traverse.MappingTraverse;

/**
 * The system reflection.
 * <p>
 * The object reflection it a link from the system to the object.
 * <p>
 * Any interaction of the system with the object is performed through the object reflection only.
 */
public interface SystemReflection extends Reflection {

  /**
   * Maps this reflection to given domain.
   *
   * @param targetDomain target domain class.
   * @return projection reference or <code>null</code>.
   * @param <TD> the target domain type.
   * @param <T> the target object reference type.
   */
  <TD, T> T mapTo(Class<TD> targetDomain);

  /**
   * Maps reflection through not parametrized channel.
   *
   * @param channelClass channel class.
   * @param qualifier channel qualifier.
   * @return target reflection.
   * @param <T> target reflection type.
   * @param <Q> channel qualifier type.
   * @param <C> channel type.
   * @throws TraverseException throws if object cannot be traversed.
   */
  <T, Q, C extends Channel1 & MappingTraverse> T mapThru(Class<C> channelClass, Q qualifier) throws TraverseException;

//  <T, R extends T> R mapThru(ChannelFunction0<? super D, T> channelFunction) throws TraverseException;

//  /**
//   * Returns <code>true</code> if this reflection is bound to the object.
//   */
//  boolean isBound();

  /**
   * Unbinds reflection and connected object.
   */
  void unbind();

  //  <R extends Reflection<D>> R as(Class<R> reflectionClass);

  /**
   * A flag indicating whether a reflection is movable or not.
   *
   * @return <code>true</code> if this reflection is movable or <code>false</code> otherwise.
   */
  default boolean isMovable() {
    return false;
  }

  /**
   * Returns movable representation of this reflection throws exception.
   */
  MovableReflection asMovableOrElseThrow();

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
  List<? extends SystemReflection> underlyingReflections();

  /**
   * Returns nearest known overlying reflection or <code>null</code> if it is unknown.
   */
  SystemReflection overlyingReflection();
}
