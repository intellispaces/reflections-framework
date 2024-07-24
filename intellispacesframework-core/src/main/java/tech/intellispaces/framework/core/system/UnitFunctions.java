package tech.intellispaces.framework.core.system;

import tech.intellispaces.framework.core.annotation.Configuration;
import tech.intellispaces.framework.core.annotation.Guide;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;

public interface UnitFunctions {

  static boolean isUnitClass(Class<?> aClass) {
    return aClass.isAnnotationPresent(Configuration.class) || aClass.isAnnotationPresent(Guide.class);
  }

  static boolean isUnitType(CustomType customType) {
    return customType.hasAnnotation(Configuration.class) || customType.hasAnnotation(Guide.class);
  }
}
