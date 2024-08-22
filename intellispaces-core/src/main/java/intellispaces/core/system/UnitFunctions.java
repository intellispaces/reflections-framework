package intellispaces.core.system;

import intellispaces.core.annotation.Configuration;
import intellispaces.core.annotation.Guide;
import intellispaces.javastatements.customtype.CustomType;

public interface UnitFunctions {

  static boolean isUnitClass(Class<?> aClass) {
    return aClass.isAnnotationPresent(Configuration.class) || aClass.isAnnotationPresent(Guide.class);
  }

  static boolean isUnitType(CustomType customType) {
    return customType.hasAnnotation(Configuration.class) || customType.hasAnnotation(Guide.class);
  }
}
