package tech.intellispaces.reflections.framework.object.reference;

import tech.intellispaces.commons.type.Type;
import tech.intellispaces.reflections.framework.channel.Channel1;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.traverse.MappingTraverse;

/**
 * The object reference.<p/>
 *
 * The object reference it a link from the system to the object.<p/>
 * Any interaction of the system with the object is performed through the object reference only.
 *
 * @param <D> the object domain type.
 */
public interface ObjectReference<D> {

  /**
   * The domain type related to this object reference.
   */
  Type<D> domain();

  /**
   * The domain class related to this object reference.
   */
  Class<?> domainAsClass();

  /**
   * Returns <code>true</code> if this object reference focused to specific object.
   */
  boolean isFocused();

  /**
   * Maps object handle to given domain.
   *
   * @param targetDomain target domain class.
   * @return projection target object handle or <code>null</code>.
   * @param <TD> the target domain type.
   * @param <T> the target object reference type.
   */
  <TD, T> T mapTo(Class<TD> targetDomain);

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
  <T, Q, C extends Channel1 & MappingTraverse> T mapThru(Class<C> channelClass, Q qualifier) throws TraverseException;

//  <T, R extends T> R mapThru(ChannelFunction0<? super D, T> channelFunction) throws TraverseException;

  /**
   * Unbinds object reference and connected object.
   */
  void unbind();
}
