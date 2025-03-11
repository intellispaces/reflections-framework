package tech.intellispaces.jaquarius.engine.description;

import tech.intellispaces.commons.action.Action;
import tech.intellispaces.jaquarius.guide.GuideKind;
import tech.intellispaces.jaquarius.object.reference.ObjectForm;
import tech.intellispaces.jaquarius.system.InjectionKind;
import tech.intellispaces.jaquarius.system.ProjectionReference;

import java.util.List;

/**
 * The unit wrapper method description.
 */
public interface UnitMethodDescription {

  /**
   * The method name.
   */
  String name();

  /**
   * Method parameter classes.
   */
  List<Class<?>> paramClasses();

  String prototypeMethodName();

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

  List<ProjectionReference> requiredProjections();

  boolean lazyLoading();

  int guideOrdinal();

  GuideKind guideKind();

  String guideCid();

  ObjectForm guideTargetForm();
}
