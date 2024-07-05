package tech.intellispaces.framework.core.annotation.processor;

import tech.intellispaces.framework.annotationprocessor.artifact.JavaArtifactContext;
import tech.intellispaces.framework.annotationprocessor.generator.TemplateBasedJavaArtifactGenerator;
import tech.intellispaces.framework.commons.action.Action;
import tech.intellispaces.framework.commons.type.TypeFunctions;
import tech.intellispaces.framework.core.common.NameFunctions;
import tech.intellispaces.framework.core.object.ObjectHandleTypes;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;
import tech.intellispaces.framework.javastatements.statement.method.MethodParam;
import tech.intellispaces.framework.javastatements.statement.method.MethodStatement;
import tech.intellispaces.framework.javastatements.statement.reference.CustomTypeReference;
import tech.intellispaces.framework.javastatements.statement.reference.NamedTypeReference;
import tech.intellispaces.framework.javastatements.statement.reference.NonPrimitiveTypeReference;
import tech.intellispaces.framework.javastatements.statement.reference.TypeReference;
import tech.intellispaces.framework.javastatements.statement.reference.WildcardTypeReference;

import javax.annotation.processing.Generated;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;
import static tech.intellispaces.framework.core.common.ActionFunctions.buildAppendSeparatorAction;

public abstract class AbstractGenerator extends TemplateBasedJavaArtifactGenerator {
  protected String generatedAnnotation;
  protected final JavaArtifactContext context = new JavaArtifactContext();

  public AbstractGenerator(CustomType annotatedType) {
    super(annotatedType);
    context.addImport(Generated.class);
  }

  @Override
  protected String canonicalName() {
    return context.generatedClassCanonicalName();
  }

  protected String sourceClassCanonicalName() {
    return annotatedType.canonicalName();
  }

  protected String sourceClassSimpleName() {
    if (annotatedType.isNested()) {
      return context.simpleNameOf(sourceClassCanonicalName());
    }
    return annotatedType.simpleName();
  }

  protected String generatedAnnotation() {
    if (generatedAnnotation == null) {
      generatedAnnotation = """
        @Generated(
          value = "%s",
          date = "%s",
          comments = "Generated with library %s"
        )""".formatted(
          this.getClass().getCanonicalName(),
          ZonedDateTime.now().format(ISO_OFFSET_DATE_TIME),
          TypeFunctions.getJavaLibraryName(this.getClass())
      );
    }
    return generatedAnnotation;
  }

  protected String buildMethodSignature(MethodStatement method) {
    return buildMethodSignature(method, method.name(), false, List.of());
  }

  protected String buildMethodSignature(MethodStatement method, String methodName) {
    return buildMethodSignature(method, methodName, false, List.of());
  }

  protected String buildMethodSignature(
      MethodStatement method, boolean includeHolderTypeParams, List<String> additionalParams
  ) {
    return buildMethodSignature(method, method.name(), includeHolderTypeParams, additionalParams);
  }

  protected String buildMethodSignature(
      MethodStatement method, String methodName, boolean includeHolderTypeParams, List<String> additionalParams
  ) {
    var signature = new StringBuilder();
    if (!method.typeParameters().isEmpty() || (includeHolderTypeParams && !method.holder().typeParameters().isEmpty())) {
      Action addCommaAction = buildAppendSeparatorAction(signature, ", ");
      signature.append("<");
      addCommaAction.execute();
      for (NamedTypeReference typeParam : method.typeParameters()) {
        signature.append(typeParam.actualDeclaration());
      }
      if (includeHolderTypeParams) {
        for (NamedTypeReference typeParam : method.holder().typeParameters()) {
          signature.append(typeParam.actualDeclaration());
        }
      }
      signature.append("> ");
    }
    TypeReference returnType = method.returnType().orElseThrow();
    context.addImports(returnType.dependencyTypenames());
    signature.append(returnType.actualDeclaration(context::simpleNameOf));
    signature.append(" ");
    signature.append(methodName);
    signature.append("(");

    Action addCommaAction = buildAppendSeparatorAction(signature, ", ");
    for (String additionalParam : additionalParams) {
      addCommaAction.execute();
      signature.append(additionalParam);
    }
    for (MethodParam param : method.params()) {
      addCommaAction.execute();
      context.addImports(param.type().dependencyTypenames());
      signature.append(param.type().actualDeclaration(context::simpleNameOf));
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
    return signature.toString();
  }

  protected String getObjectHandleCanonicalName(TypeReference domainType, ObjectHandleTypes handleType) {
    if (domainType.asPrimitiveTypeReference().isPresent()) {
      return domainType.asPrimitiveTypeReference().get().typename();
    } else if (domainType.asNamedTypeReference().isPresent()) {
      return domainType.asNamedTypeReference().get().name();
    } else if (domainType.asCustomTypeReference().isPresent()) {
      CustomTypeReference customTypeReference = domainType.asCustomTypeReference().get();
      CustomType targetType = customTypeReference.targetType();
      if (targetType.canonicalName().equals(Class.class.getCanonicalName())) {
        var sb = new StringBuilder();
        sb.append(Class.class.getSimpleName());
        if (!customTypeReference.typeArguments().isEmpty()) {
          sb.append("<");
          Action addCommaAction = buildAppendSeparatorAction(sb, ", ");
          for (NonPrimitiveTypeReference argType : customTypeReference.typeArguments()) {
            addCommaAction.execute();
            sb.append(argType.actualDeclaration());
//            sb.append(getObjectHandleCanonicalName(argType, ObjectHandleTypes.Common));
          }
          sb.append(">");
        }
        return sb.toString();
      } else if (targetType.canonicalName().startsWith("java.lang.")) {
        return targetType.simpleName();
      } else {
        var sb = new StringBuilder();
        String canonicalName = NameFunctions.getObjectHandleTypename(targetType.className(), handleType);
        context.addImport(canonicalName);
        String simpleName = context.simpleNameOf(canonicalName);
        sb.append(simpleName);
        if (!customTypeReference.typeArguments().isEmpty()) {
          sb.append("<");
          Action addCommaAction = buildAppendSeparatorAction(sb, ", ");
          for (NonPrimitiveTypeReference argType : customTypeReference.typeArguments()) {
            addCommaAction.execute();
            sb.append(argType.actualDeclaration());
          }
          sb.append(">");
        }
        return sb.toString();
      }
    } else if (domainType.asWildcardTypeReference().isPresent()) {
      WildcardTypeReference wildcardTypeReference = domainType.asWildcardTypeReference().get();
      if (wildcardTypeReference.extendedBound().isPresent()) {
        return getObjectHandleCanonicalName(wildcardTypeReference.extendedBound().get(), handleType);
      } else {
        throw new UnsupportedOperationException("Not implemented");
      }
    } else if (domainType.asArrayTypeReference().isPresent()) {
      TypeReference elementType = domainType.asArrayTypeReference().get().elementType();
      return getObjectHandleCanonicalName(elementType, handleType) + "[]";
    } else {
      throw new UnsupportedOperationException("Unsupported type - " + domainType.actualDeclaration());
    }
  }
}
