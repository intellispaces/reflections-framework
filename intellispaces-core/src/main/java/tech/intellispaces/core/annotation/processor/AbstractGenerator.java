package tech.intellispaces.core.annotation.processor;

import tech.intellispaces.actions.Executor;
import tech.intellispaces.actions.string.StringActions;
import tech.intellispaces.annotations.artifact.JavaArtifactContext;
import tech.intellispaces.annotations.generator.TemplateBasedJavaArtifactGenerator;
import tech.intellispaces.commons.type.TypeFunctions;
import tech.intellispaces.core.object.ObjectFunctions;
import tech.intellispaces.core.object.ObjectHandleTypes;
import tech.intellispaces.javastatements.customtype.CustomType;
import tech.intellispaces.javastatements.method.MethodParam;
import tech.intellispaces.javastatements.method.MethodSignatureDeclarations;
import tech.intellispaces.javastatements.method.MethodStatement;
import tech.intellispaces.javastatements.reference.CustomTypeReference;
import tech.intellispaces.javastatements.reference.NotPrimitiveReference;
import tech.intellispaces.javastatements.reference.TypeReference;
import tech.intellispaces.javastatements.reference.WildcardReference;

import javax.annotation.processing.Generated;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

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

  protected String makeGeneratedAnnotation() {
    if (generatedAnnotation == null) {
      generatedAnnotation = """
        @Generated(
          value = "Source artifact %s" +
                  "Generated with library %s" +
                  "Generator %s",
          date = "%s"
        )""".formatted(
          annotatedType.canonicalName(),
          TypeFunctions.getJavaLibraryName(this.getClass()),
          this.getClass().getCanonicalName(),
          ZonedDateTime.now().format(ISO_OFFSET_DATE_TIME)
      );
    }
    return generatedAnnotation;
  }

  protected String buildGeneratedMethodJavadoc(String sourceClassCanonicalName, MethodStatement method) {
    return """
      /**
       * Based on the method {@link %s#%s(%s)}
       */
        """.formatted(
          sourceClassCanonicalName,
          method.name(),
          method.params().stream()
              .map(MethodParam::type)
              .map(type -> type.actualDeclaration(context::addToImportAndGetSimpleName))
              .collect(Collectors.joining(", "))
    );
  }

  protected String buildMethodSignature(MethodStatement method) {
    return buildMethodSignature(method, method.name(), true, false, List.of());
  }

  protected String buildMethodSignature(MethodStatement method, String methodName) {
    return buildMethodSignature(method, methodName, true, false, List.of());
  }

  protected String buildMethodSignature(MethodStatement method, List<String> additionalParams) {
    return buildMethodSignature(method, method.name(), true, false, additionalParams);
  }

  protected String buildMethodSignature(
      MethodStatement method,
      String methodName,
      boolean includeMethodTypeParams,
      boolean includeOwnerTypeParams,
      List<String> additionalParams
  ) {
    return MethodSignatureDeclarations.build(method)
        .methodName(methodName)
        .includeMethodTypeParams(includeMethodTypeParams)
        .includeOwnerTypeParams(includeOwnerTypeParams)
        .addAdditionalParams(additionalParams)
        .get(context::addImport, context::simpleNameOf);
  }

  protected String getObjectHandleDeclaration(TypeReference domainType, ObjectHandleTypes handleType) {
    if (domainType.asPrimitiveReference().isPresent()) {
      return domainType.asPrimitiveReference().get().typename();
    } else if (domainType.asNamedReference().isPresent()) {
      return domainType.asNamedReference().get().name();
    } else if (domainType.asCustomTypeReference().isPresent()) {
      CustomTypeReference customTypeReference = domainType.asCustomTypeReference().get();
      CustomType targetType = customTypeReference.targetType();
      if (targetType.canonicalName().equals(Class.class.getCanonicalName())) {
        var sb = new StringBuilder();
        sb.append(Class.class.getSimpleName());
        if (!customTypeReference.typeArguments().isEmpty()) {
          sb.append("<");
          Executor commaAppender = StringActions.commaAppender(sb);
          for (NotPrimitiveReference argType : customTypeReference.typeArguments()) {
            commaAppender.execute();
            sb.append(argType.actualDeclaration());
          }
          sb.append(">");
        }
        return sb.toString();
      } else if (targetType.canonicalName().startsWith("java.lang.")) {
        return targetType.simpleName();
      } else {
        var sb = new StringBuilder();
        String canonicalName = ObjectFunctions.getObjectHandleTypename(targetType, handleType);
        context.addImport(canonicalName);
        String simpleName = context.simpleNameOf(canonicalName);
        sb.append(simpleName);
        if (!customTypeReference.typeArguments().isEmpty()) {
          sb.append("<");
          Executor commaAppender = StringActions.commaAppender(sb);
          for (NotPrimitiveReference argType : customTypeReference.typeArguments()) {
            commaAppender.execute();
            sb.append(getObjectHandleDeclaration(argType, ObjectHandleTypes.Common));
          }
          sb.append(">");
        }
        return sb.toString();
      }
    } else if (domainType.asWildcard().isPresent()) {
      WildcardReference wildcardTypeReference = domainType.asWildcard().get();
      if (wildcardTypeReference.extendedBound().isPresent()) {
        return getObjectHandleDeclaration(wildcardTypeReference.extendedBound().get(), handleType);
      } else {
        return Object.class.getCanonicalName();
      }
    } else if (domainType.asArrayReference().isPresent()) {
      TypeReference elementType = domainType.asArrayReference().get().elementType();
      return getObjectHandleDeclaration(elementType, handleType) + "[]";
    } else {
      throw new UnsupportedOperationException("Unsupported type - " + domainType.actualDeclaration());
    }
  }
}
