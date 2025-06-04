package tech.intellispaces.reflections.framework.system;

import java.util.List;

import tech.intellispaces.actions.Action;
import tech.intellispaces.core.Rid;
import tech.intellispaces.reflections.framework.guide.GuideKind;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;

/**
 * The unit wrapper method description.
 */
public interface UnitMethod {

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

  Rid guideChannelId();

  ReflectionForm guideTargetForm();
}
