package tech.intellispacesframework.core.system;

import tech.intellispacesframework.commons.exception.UnexpectedViolationException;
import tech.intellispacesframework.core.annotation.Projection;
import tech.intellispacesframework.core.annotation.Wrapper;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;

public interface UnitWrapper {

  List<Injection> getInjections();

  static String getWrapperClassCanonicalName(String unitClassName) {
    return unitClassName.replace('$', '_') + "Wrapper";
  }

  static Method getActualMethod(Method wrapperMethod) {
    try {
      Class<?> wrapperClass = wrapperMethod.getDeclaringClass();
      if (!wrapperClass.isAnnotationPresent(Wrapper.class)) {
        throw UnexpectedViolationException.withMessage("Expected unit wrapper class");
      }
      Class<?> unitClass = wrapperClass.getSuperclass();

      if (wrapperMethod.isAnnotationPresent(Projection.class) && wrapperMethod.getName().startsWith("_")) {
        return unitClass.getMethod(wrapperMethod.getName().substring(1),
            Arrays.stream(wrapperMethod.getParameters()).map(Parameter::getType).toArray(Class<?>[]::new));
      } else {
        return wrapperMethod;
      }
    } catch (NoSuchMethodException | SecurityException e) {
      throw UnexpectedViolationException.withCauseAndMessage(e, "Failed to get actual method of unit wrapper method {}",
          wrapperMethod);
    }
  }
}
