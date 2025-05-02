package tech.intellispaces.jaquarius.system;

import tech.intellispaces.jaquarius.annotation.Configuration;
import tech.intellispaces.jaquarius.annotation.Guide;
import tech.intellispaces.statementsj.customtype.CustomType;

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
