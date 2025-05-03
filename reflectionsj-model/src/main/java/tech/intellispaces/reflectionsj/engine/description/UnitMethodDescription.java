package tech.intellispaces.reflectionsj.engine.description;

import java.util.List;

import tech.intellispaces.actions.Action;
import tech.intellispaces.reflectionsj.guide.GuideKind;
import tech.intellispaces.reflectionsj.object.reference.ObjectReferenceForm;
import tech.intellispaces.reflectionsj.system.InjectionKind;
import tech.intellispaces.reflectionsj.system.ProjectionReference;

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

  String guideChannelId();

  ObjectReferenceForm guideTargetForm();
}
