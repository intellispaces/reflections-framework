package tech.intellispacesframework.core.annotation.processor.domain;

import tech.intellispacesframework.commons.action.Action;
import tech.intellispacesframework.core.annotation.processor.AbstractGenerator;
import tech.intellispacesframework.javastatements.statement.custom.CustomType;
import tech.intellispacesframework.javastatements.statement.custom.MethodParam;
import tech.intellispacesframework.javastatements.statement.custom.MethodStatement;
import tech.intellispacesframework.javastatements.statement.reference.CustomTypeReference;
import tech.intellispacesframework.javastatements.statement.reference.NonPrimitiveTypeReference;
import tech.intellispacesframework.javastatements.statement.reference.TypeReference;
import tech.intellispacesframework.javastatements.statement.reference.WildcardTypeReference;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static tech.intellispacesframework.core.util.Actions.addSeparatorAction;

abstract class AbstractObjectHandleGenerator extends AbstractGenerator {
  protected String domainTypeParamsFull;
  protected String domainTypeParamsBrief;
  protected List<String> methods;

  AbstractObjectHandleGenerator(CustomType domainType) {
    super(domainType);
  }

  protected void analyzeObjectHandleMethods(CustomType domainType) {
    this.methods = domainType.declaredMethods().stream()
        .map(this::buildMethodSignature)
        .toList();
  }

  private String buildMethodSignature(MethodStatement method) {
    var signature = new StringBuilder();
    if (method.returnType().isEmpty()) {
      signature.append("void");
    } else {
      TypeReference domainReturnType = method.returnType().get();
      signature.append(getHandleTypename(domainReturnType, context.getImportConsumer()));
      signature.append(" ");
      signature.append(method.name());
      signature.append("(");

      Action addCommaAction = addSeparatorAction(signature, ", ");
      for (MethodParam param : method.params()) {
        addCommaAction.execute();
        signature.append(getHandleTypename(param.type(), context.getImportConsumer()));
        signature.append(" ");
        signature.append(param.name());
      }
      signature.append(")");

      String exceptions = method.exceptions().stream()
          .map(e -> e.asCustomTypeReference().orElseThrow().targetType())
          .peek(e -> context.addImport(e.canonicalName()))
          .map(e -> context.simpleNameOf(e.canonicalName()))
          .collect(Collectors.joining(", "));
      if (!exceptions.isEmpty()) {
        signature.append(" throws ").append(exceptions);
      }
    }
    return signature.toString();
  }

  private String getHandleTypename(TypeReference type, Consumer<String> imports) {
    if (type.asPrimitiveTypeReference().isPresent()) {
      return type.asPrimitiveTypeReference().get().typename();
    } else if (type.asNamedTypeReference().isPresent()) {
      return type.asNamedTypeReference().get().name();
    } else if (type.asCustomTypeReference().isPresent()) {
      CustomTypeReference customTypeReference = type.asCustomTypeReference().get();
      CustomType targetType = customTypeReference.targetType();
      if (targetType.canonicalName().startsWith("java.lang.")) {
        return targetType.simpleName();
      } else {
        var sb = new StringBuilder();
        String canonicalName = targetType.canonicalName() + "Handle";
        imports.accept(canonicalName);
        sb.append(context.simpleNameOf(canonicalName));
        if (!customTypeReference.typeArguments().isEmpty()) {
          sb.append("<");
          Action addCommaAction = addSeparatorAction(sb, ", ");
          for (NonPrimitiveTypeReference argType : customTypeReference.typeArguments()) {
            addCommaAction.execute();
            sb.append(getHandleTypename(argType, imports));
          }
          sb.append(">");
        }
        return sb.toString();
      }
    } else if (type.asWildcardTypeReference().isPresent()) {
      WildcardTypeReference wildcardTypeReference = type.asWildcardTypeReference().get();
      if (wildcardTypeReference.extendedBound().isPresent()) {
        return getHandleTypename(wildcardTypeReference.extendedBound().get(), imports);
      } else {
        throw new UnsupportedOperationException("Not implemented");
      }
    } else if (type.asArrayTypeReference().isPresent()) {
      TypeReference elementType = type.asArrayTypeReference().get().elementType();
      return getHandleTypename(elementType, imports) + "[]";
    } else {
      throw new UnsupportedOperationException("Unsupported type - " + type.actualDeclaration());
    }
  }
}
