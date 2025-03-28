package tech.intellispaces.jaquarius.engine.description;

import tech.intellispaces.commons.action.Action;
import tech.intellispaces.commons.type.Type;

import java.util.List;

/**
 * The object provider method description.
 */
public interface ObjectProviderMethodDescription {

  /**
   * The object provider instance.
   */
  Object objectProvider();

  /**
   * The method name.
   */
  String name();

  /**
   * The returned object type.
   */
  Type<?> returnedType();

  /**
   * The returned object domain class.
   */
  Class<?> returnedDomainClass();

  /**
   * Method parameter types.
   */
  List<Type<?>> paramTypes();

  /**
   * Related action.
   */
  Action action();
}