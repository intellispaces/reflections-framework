package intellispaces.framework.core.object;

import intellispaces.common.base.type.Type;
import intellispaces.framework.core.exception.TraverseException;
import intellispaces.framework.core.space.transition.Transition1;

/**
 * Handle of object.<p/>
 *
 * The handle implements interaction with the object.<p/>
 *
 * The interaction of the system with the object is performed through the object handle.<p/>
 *
 * @param <D> object domain type.
 */
public interface ObjectHandle<D> {

  /**
   * Returns the type of the primary domain declaration.
   */
  Type<D> domain();

  /**
   * Returns the class declaring the domain.
   */
  Class<?> domainClass();

  boolean isMovable();

  MovableObjectHandle<D> asMovableOrElseThrow();

  @SuppressWarnings("rawtypes")
  <T, Q> T mapThru(Class<? extends Transition1> transitionClass, Q qualifier) throws TraverseException;
}
