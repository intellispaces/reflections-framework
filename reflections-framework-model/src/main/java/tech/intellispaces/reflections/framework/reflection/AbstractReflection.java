package tech.intellispaces.reflections.framework.reflection;

import tech.intellispaces.commons.type.Type;
import tech.intellispaces.reflections.framework.channel.Channel1;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.traverse.MappingTraverse;

/**
 * The abstract object reflection.<p/>
 *
 * The object reflection it a link from the system to the object.<p/>
 * Any interaction of the system with the object is performed through the object reflection only.
 *
 * @param <D> the domain type.
 */
public interface AbstractReflection<D> {

  /**
   * The domain type related to this reflection.
   */
  Type<D> domainType();

  /**
   * The domain class related to this reflection.
   */
  Class<?> domainClass();

  /**
   * Returns <code>true</code> if this reflection is focused.
   */
  boolean isFocused();

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
}
