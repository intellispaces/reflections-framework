package tech.intellispaces.reflections.framework.factory;

import tech.intellispaces.commons.text.StringFunctions;
import tech.intellispaces.jstatements.method.MethodStatement;

public interface FactoryFunctions {

  static String getContractType(MethodStatement method) {
    return getContractType(method.name());
  }

  static String getContractType(String methodName) {
    if (methodName.equals("create") || (methodName.startsWith("create") && StringFunctions.isUppercaseChar(methodName, 6))) {
      return "create";
    } else if (methodName.equals("reflectionOf") || (methodName.startsWith("reflectionOf") && StringFunctions.isUppercaseChar(methodName, 12))) {
      return "reflection";
    }
    return methodName;
  }
}
