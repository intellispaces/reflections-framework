package tech.intellispaces.reflections.framework.factory;

import java.util.List;

import tech.intellispaces.actions.Action;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.core.Domain;

/**
 * The factory method description.
 */
public interface FactoryMethod {

  /**
   * The factory instance.
   */
  Object factoryInstance();

  /**
   * The contract type.
   */
  String contractType();

  /**
   * The returned reflection type.
   */
  Type<?> returnedType();

  /**
   * The returned object domain.
   */
  Domain returnedDomain();

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