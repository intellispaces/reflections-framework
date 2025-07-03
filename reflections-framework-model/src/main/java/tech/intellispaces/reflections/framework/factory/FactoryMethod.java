package tech.intellispaces.reflections.framework.factory;

import java.util.List;

import tech.intellispaces.actions.Action;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.reflections.framework.system.ReflectionFactory;

/**
 * The factory method description.
 */
public interface FactoryMethod extends ReflectionFactory {

  /**
   * The factory instance.
   */
  Object factoryInstance();

  /**
   * The returned reflection type.
   */
  Type<?> returnedType();

  /**
   * Contract qualifier names.
   */
  List<String> contractQualifierNames();

  /**
   * Contract qualifier types.
   */
  List<Type<?>> contractQualifierTypes();

  /**
   * Related action.
   */
  Action action();
}