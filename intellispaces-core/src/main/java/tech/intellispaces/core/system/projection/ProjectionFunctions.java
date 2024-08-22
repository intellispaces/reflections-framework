package tech.intellispaces.core.system.projection;

import tech.intellispaces.commons.exception.UnexpectedViolationException;
import tech.intellispaces.core.annotation.Projection;
import tech.intellispaces.javastatements.method.MethodStatement;

import java.lang.reflect.Method;

public interface ProjectionFunctions {

  static String getProjectionName(MethodStatement projectionMethod) {
    Projection annotation = projectionMethod.selectAnnotation(Projection.class).orElseThrow(() ->
        UnexpectedViolationException.withMessage("Projection method should be marked with annotation {}",
        Projection.class.getSimpleName()));
    return getProjectionName(projectionMethod.name(), annotation);
  }

  static String getProjectionName(Method projectionMethod) {
    Projection annotation = projectionMethod.getAnnotation(Projection.class);
    if (annotation == null) {
      throw UnexpectedViolationException.withMessage("Projection method should be marked with annotation {}",
          Projection.class.getSimpleName());
    }
    return getProjectionName(projectionMethod.getName(), annotation);
  }

  private static String getProjectionName(String methodName, Projection annotation) {
    String name = annotation.value();
    if ("".equals(name)) {
      name = methodName;
    }
    return name;
  }
}
