package tech.intellispaces.reflectionsframework.engine.description;

import java.util.List;

import tech.intellispaces.actions.Action;
import tech.intellispaces.commons.type.Type;

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