package tech.intellispaces.jaquarius.annotation.processor.domain;

import tech.intellispaces.java.reflection.customtype.CustomType;
import tech.intellispaces.java.reflection.method.MethodStatement;
import tech.intellispaces.java.reflection.reference.NamedReference;

public interface DomainGenerationFunctions {

  static String getChannelTypeParams(CustomType domain, MethodStatement channelMethod) {
    if (domain.typeParameters().isEmpty() && channelMethod.typeParameters().isEmpty()) {
      return "";
    } else if (!domain.typeParameters().isEmpty() && channelMethod.typeParameters().isEmpty()) {
      return domain.typeParametersFullDeclaration();
    } else if (domain.typeParameters().isEmpty() && !channelMethod.typeParameters().isEmpty()) {
      return getChannelMethodTypeParamsDeclaration(channelMethod);
    } else {
      String domainTypeParams = domain.typeParametersFullDeclaration();
      String channelTypeParams = getChannelMethodTypeParamsDeclaration(channelMethod);
      return domainTypeParams.substring(0, domainTypeParams.length() - 1) +
          ", " + channelTypeParams.substring(1);
    }
  }

  private static String getChannelMethodTypeParamsDeclaration(MethodStatement channelMethod) {
    var sb = new StringBuilder();
    sb.append("<");
    boolean first = true;
    for (NamedReference typeParam : channelMethod.typeParameters()) {
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
