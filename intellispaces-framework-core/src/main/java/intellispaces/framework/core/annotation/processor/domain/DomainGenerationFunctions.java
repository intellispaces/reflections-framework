package intellispaces.framework.core.annotation.processor.domain;

import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.common.javastatement.reference.NamedReference;

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
