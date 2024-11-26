package tech.intellispaces.jaquarius.object.reference;

import tech.intellispaces.jaquarius.channel.Channel1;
import tech.intellispaces.jaquarius.channel.MappingChannel;
import tech.intellispaces.jaquarius.exception.TraverseException;
import tech.intellispaces.entity.type.Type;

/**
 * Object reference.<p/>
 *
 * The object reference connects the system, the object and the semantic domain.<p/>
 *
 * Any interaction of the system with the object is performed through the object reference.
 *
 * @param <D> the related domain type.
 */
public interface ObjectReference<D> {

  /**
   * Type of the domain related to this handle.
   */
  Type<D> domain();

  /**
   * Class of the domain related to this handle.
   */
  Class<?> domainClass();

  /**
   * Returns <code>true</code> if this object reference focused to specific object.
   */
  boolean isFocused();

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

  /**
   * Releases all resources associated with this object reference, if any.
   */
  void release();
}
