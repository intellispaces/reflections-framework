package tech.intellispaces.core.system;

import tech.intellispaces.core.annotation.Configuration;
import tech.intellispaces.core.annotation.Guide;
import tech.intellispaces.javastatements.customtype.CustomType;

public interface UnitFunctions {

  static boolean isUnitClass(Class<?> aClass) {
    return aClass.isAnnotationPresent(Configuration.class) || aClass.isAnnotationPresent(Guide.class);
  }

  static boolean isUnitType(CustomType customType) {
    return customType.hasAnnotation(Configuration.class) || customType.hasAnnotation(Guide.class);
  }
}
