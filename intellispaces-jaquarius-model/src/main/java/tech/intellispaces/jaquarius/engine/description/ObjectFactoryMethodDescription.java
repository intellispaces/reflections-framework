package tech.intellispaces.jaquarius.engine.description;

import tech.intellispaces.actions.Action;
import tech.intellispaces.commons.type.Type;

import java.util.List;

/**
 * The object factory method description.
 */
public interface ObjectFactoryMethodDescription {

  /**
   * The object factory instance.
   */
  Object objectFactory();

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