package tech.intellispaces.framework.core.system;

import tech.intellispaces.framework.core.annotation.Configuration;
import tech.intellispaces.framework.core.annotation.Guide;

public interface UnitFunctions {

  static boolean isUnitClass(Class<?> preprocessingClass) {
    return preprocessingClass.isAnnotationPresent(Configuration.class) ||
        preprocessingClass.isAnnotationPresent(Guide.class);
  }
}
