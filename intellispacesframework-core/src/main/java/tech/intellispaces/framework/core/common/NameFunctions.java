package tech.intellispaces.framework.core.common;

public interface NameFunctions {

  static String getObjectHandleClassCanonicalName(String domainClassName) {
    return transformClassName(domainClassName) + "Handle";
  }

  static String getMovableObjectHandleClassCanonicalName(String domainClassName) {
    return transformClassName(domainClassName) + "HandleMovable";
  }

  static String getUnitWrapperCanonicalName(String unitClassName) {
    return transformClassName(unitClassName) + "Wrapper";
  }

  static String getDataClassCanonicalName(String domainClassName) {
    return transformClassName(domainClassName) + "HandleImpl";
  }

  private static String transformClassName(String className) {
    return className.replace("$", "");
  }
}
