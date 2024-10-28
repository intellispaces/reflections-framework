package intellispaces.jaquarius.object;

import intellispaces.common.base.type.Type;
import intellispaces.jaquarius.channel.Channel1;
import intellispaces.jaquarius.channel.MappingChannel;
import intellispaces.jaquarius.exception.TraverseException;

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

  /**
   * Releases all direct links to the actual object, if any.
   */
  void release();

  /**
   * Add projection.
   *
   * @param targetDomain target domain class.
   * @param target target object handle.
   * @param <TD> target domain type.
   * @param <TH> target object handle type.
   */
  <TD, TH> void addProjection(Class<TD> targetDomain, TH target);

  /**
   * Maps object handle to given domain.
   *
   * @param targetDomain target domain class.
   * @return projection target object handle or <code>null</code>.
   * @param <TD> target domain type.
   * @param <TH> target object handle type.
   */
  <TD, TH> TH mapTo(Class<TD> targetDomain);

  /**
   * Maps object through not parametrized channel.
   *
   * @param channelClass channel class.
   * @param qualifier channel qualifier.
   * @return target object handle.
   * @param <T> target object handle type.
   * @param <Q> channel qualifier type.
   * @param <C> channel type.
   * @throws TraverseException throws if object cannot be traversed.
   */
  <T, Q, C extends Channel1 & MappingChannel> T mapThru(Class<C> channelClass, Q qualifier) throws TraverseException;
}
