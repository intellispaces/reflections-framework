package tech.intellispaces.jaquarius.engine.descriptor;

import tech.intellispaces.action.Action;
import tech.intellispaces.jaquarius.traverse.TraverseType;

import java.util.List;

/**
 * Object handle wrapper method descriptor.
 */
public interface ObjectHandleMethod {

  /**
   * The method name.
   */
  String name();

  /**
   * Method parameter classes.
   */
  List<Class<?>> paramClasses();

  /**
   * The method purpose.
   */
  ObjectHandleMethodPurpose purpose();

  /**
   * The traverse method ordinal.
   */
  int traverseOrdinal();

  /**
   * Related action.
   */
  Action action();

  /**
   * The channel class related to this method.
   */
  Class<?> channelClass();

  /**
   * The traverse type related to this method.
   */
  TraverseType traverseType();

  /**
   * The injection kind.
   */
  String injectionKind();

  /**
   * The injection method ordinal.
   */
  int injectionOrdinal();

  /**
   * The injection name.
   */
  String injectionName();

  /**
   * The injection type.
   */
  Class<?> injectionType();
}
