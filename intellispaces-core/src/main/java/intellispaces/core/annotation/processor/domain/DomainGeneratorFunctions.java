package intellispaces.core.annotation.processor.domain;

import intellispaces.javastatements.customtype.CustomType;
import intellispaces.javastatements.method.MethodStatement;
import intellispaces.javastatements.reference.NamedReference;

public interface DomainGeneratorFunctions {

  static String getTransitionTypeParams(CustomType domain, MethodStatement transitionMethod) {
    if (domain.typeParameters().isEmpty() && transitionMethod.typeParameters().isEmpty()) {
      return "";
    } else if (!domain.typeParameters().isEmpty() && transitionMethod.typeParameters().isEmpty()) {
      return domain.typeParametersFullDeclaration();
    } else if (domain.typeParameters().isEmpty() && !transitionMethod.typeParameters().isEmpty()) {
      return getTransitionMethodTypeParamsDeclaration(transitionMethod);
    } else {
      String domainTypeParams = domain.typeParametersFullDeclaration();
      String transitionTypeParams = getTransitionMethodTypeParamsDeclaration(transitionMethod);
      return domainTypeParams.substring(0, domainTypeParams.length() - 1) +
          ", " +
          transitionTypeParams.substring(1);
    }
  }

  private static String getTransitionMethodTypeParamsDeclaration(MethodStatement transitionMethod) {
    var sb = new StringBuilder();
    sb.append("<");
    boolean first = true;
    for (NamedReference typeParam : transitionMethod.typeParameters()) {
      if (!first) {
        sb.append(", ");
      }
      sb.append(typeParam.actualDeclaration());
      first = false;
    }
    sb.append(">");
    return sb.toString();
  }
}
