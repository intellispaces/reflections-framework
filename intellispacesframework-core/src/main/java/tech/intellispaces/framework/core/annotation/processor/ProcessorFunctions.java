package tech.intellispaces.framework.core.annotation.processor;

import tech.intellispaces.framework.commons.type.TypeFunctions;
import tech.intellispaces.framework.javastatements.statement.reference.TypeReference;

public interface ProcessorFunctions {

  static String getDomainClassLink(TypeReference type) {
    if (type.isPrimitive()) {
      return "{@link " +
          TypeFunctions.getPrimitiveWrapperClass(type.asPrimitiveTypeReferenceSurely().typename()).getSimpleName() +
          "}";
    } else if (type.isCustomTypeReference()) {
      return "{@link " + type.asCustomTypeReferenceSurely().targetType().simpleName() + "}";
    } else {
      return "Object";
    }
  }

  static boolean isVoidType(TypeReference type) {
    if (type.isCustomTypeReference()) {
      return Void.class.getCanonicalName().equals(type.asCustomTypeReferenceSurely().targetType().canonicalName());
    }
    return false;
  }
}
