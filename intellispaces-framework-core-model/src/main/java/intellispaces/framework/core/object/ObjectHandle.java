package intellispaces.framework.core.object;

import intellispaces.common.base.type.Type;
import intellispaces.framework.core.exception.TraverseException;
import intellispaces.framework.core.space.channel.Channel1;
import intellispaces.framework.core.space.channel.MappingChannel;

/**
 * Handle of object.<p/>
 *
 * Object handle binds an object to a semantic domain.<p/>
 *
 * The handle implements interaction with the object.
 * Any interaction of the system with the object is performed through the object handle.<p/>
 *
 * @param <D> related domain type.
 */
public interface ObjectHandle<D> {

  /**
   * Type of the domain related to this handle.
   */
  Type<D> domain();

  /**
   * Class of the domain related to this handle.
   */
  Class<?> domainClass();

  boolean isMovable();

  MovableObjectHandle<D> asMovableOrElseThrow();

  <T, Q, C extends Channel1 & MappingChannel> T mapThru(Class<C> channelClass, Q qualifier) throws TraverseException;
}
