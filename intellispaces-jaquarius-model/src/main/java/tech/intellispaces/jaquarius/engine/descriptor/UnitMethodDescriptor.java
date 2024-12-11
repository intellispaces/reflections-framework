package tech.intellispaces.jaquarius.engine.descriptor;

import tech.intellispaces.action.Action;
import tech.intellispaces.jaquarius.system.InjectionKind;

import java.util.List;

/**
 * The unit wrapper method descriptor.
 */
public interface UnitMethodDescriptor {

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
  UnitMethodPurpose purpose();

  /**
   * The related action.
   */
  Action action();

  String injectionName();

  Class<?> injectionClass();

  int injectionOrdinal();

  InjectionKind injectionKind();

  String projectionName();

  Class<?> targetClass();

  boolean lazyLoading();

  int guideOrdinal();
}
