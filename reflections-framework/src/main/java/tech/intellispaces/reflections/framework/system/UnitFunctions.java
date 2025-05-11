package tech.intellispaces.reflections.framework.system;

import tech.intellispaces.reflections.framework.annotation.Configuration;
import tech.intellispaces.reflections.framework.annotation.Guide;
import tech.intellispaces.jstatements.customtype.CustomType;

public interface UnitFunctions {

  static boolean isUnitClass(Class<?> aClass) {
    return isGuideUnit(aClass) || isConfigurationUnit(aClass);
  }

  static boolean isUnitType(CustomType type) {
    return type.hasAnnotation(Configuration.class) || type.hasAnnotation(Guide.class);
  }

  static boolean isGuideUnit(Class<?> aClass) {
    return aClass.isAnnotationPresent(Guide.class);
  }

  static boolean isGuideInterface(CustomType type) {
    return type.asInterface().isPresent() && type.hasAnnotation(Guide.class);
  }

  static boolean isConfigurationUnit(Class<?> aClass) {
    return aClass.isAnnotationPresent(Configuration.class);
  }
}
