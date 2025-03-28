package tech.intellispaces.jaquarius.object.provider;

import tech.intellispaces.commons.reflection.method.MethodStatement;
import tech.intellispaces.commons.text.StringFunctions;

public interface ObjectProviderFunctions {

  static String getContractType(MethodStatement method) {
    return getContractType(method.name());
  }

  static String getContractType(String methodName) {
    if (methodName.equals("create") || (methodName.startsWith("create") && StringFunctions.isUppercaseChar(methodName, 6))) {
      return "create";
    } else if (methodName.equals("handleOf") || (methodName.startsWith("handleOf") && StringFunctions.isUppercaseChar(methodName, 8))) {
      return "handle";
    }
    return methodName;
  }
}
