package tech.intellispaces.reflections.framework.factory;

import java.util.List;

import tech.intellispaces.actions.Action;
import tech.intellispaces.commons.type.Type;

/**
 * The factory method description.
 */
public interface FactoryMethod {

  /**
   * The factory instance.
   */
  Object factoryInstance();

  /**
   * The method name.
   */
  String name();

  /**
   * The returned reflection type.
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