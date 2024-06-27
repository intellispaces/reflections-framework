package tech.intellispaces.framework.core.common;

import tech.intellispaces.framework.core.object.ObjectHandleTypes;

public interface NameFunctions {

  static String getObjectHandleClassCanonicalName(String domainClassName, ObjectHandleTypes handleType) {
    return switch (handleType) {
      case Common -> getCommonObjectHandleClassCanonicalName(domainClassName);
      case Movable -> getMovableObjectHandleClassCanonicalName(domainClassName);
      case Unmovable -> getUnmovableObjectHandleClassCanonicalName(domainClassName);
    };
  }

  static String getCommonObjectHandleClassCanonicalName(String domainClassName) {
    return transformClassName(domainClassName) + "Handle";
  }

  static String getMovableObjectHandleClassCanonicalName(String domainClassName) {
    return transformClassName(domainClassName) + "MovableHandle";
  }

  static String getUnmovableObjectHandleClassCanonicalName(String domainClassName) {
    return transformClassName(domainClassName) + "UnmovableHandle";
  }

  static String getUnitWrapperCanonicalName(String unitClassName) {
    return transformClassName(unitClassName) + "Wrapper";
  }

  static String getDataClassCanonicalName(String domainClassName) {
    return transformClassName(domainClassName) + "HandleImpl";
  }

  static String transformClassName(String className) {
    return className.replace("$", "");
  }
}
