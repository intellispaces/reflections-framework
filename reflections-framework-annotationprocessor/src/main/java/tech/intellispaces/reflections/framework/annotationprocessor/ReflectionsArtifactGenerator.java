package tech.intellispaces.reflections.framework.annotationprocessor;

import java.util.List;
import java.util.stream.Collectors;

import tech.intellispaces.annotationprocessor.TemplatedJavaArtifactGenerator;
import tech.intellispaces.commons.type.ClassFunctions;
import tech.intellispaces.javareflection.customtype.CustomType;
import tech.intellispaces.javareflection.method.MethodParam;
import tech.intellispaces.javareflection.method.MethodSignatureDeclarations;
import tech.intellispaces.javareflection.method.MethodStatement;
import tech.intellispaces.javareflection.reference.TypeReference;
import tech.intellispaces.reflections.framework.reflection.MovabilityType;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.reflection.ReflectionFunctions;

public abstract class ReflectionsArtifactGenerator extends TemplatedJavaArtifactGenerator {

  public ReflectionsArtifactGenerator(CustomType sourceArtifact) {
    super(sourceArtifact);
  }

  protected String buildMethodSignature(MethodStatement method) {
    return buildMethodSignature(method, method.name(), false, List.of());
  }

  protected String buildMethodSignature(MethodStatement method, String newMethodName) {
    return buildMethodSignature(method, newMethodName, false, List.of());
  }

  protected String buildMethodSignatureIncludedOwnerTypeParams(MethodStatement method) {
    return buildMethodSignature(method, method.name(), true, List.of());
  }

  protected String buildMethodSignatureIncludedOwnerTypeParams(MethodStatement method, List<String> additionalParams) {
    return buildMethodSignature(method, method.name(), true, additionalParams);
  }

  private String buildMethodSignature(
      MethodStatement method,
      String methodName,
      boolean includeOwnerTypeParams,
      List<String> additionalParams
  ) {
    return MethodSignatureDeclarations.build(method)
        .methodName(methodName)
        .includeMethodTypeParams(true)
        .includeOwnerTypeParams(includeOwnerTypeParams)
        .addAdditionalParams(additionalParams)
        .get(this::addImport, this::simpleNameOf);
  }

  protected String buildObjectFormDeclaration(
          TypeReference domainType, ReflectionForm objectForm, MovabilityType movabilityType, boolean replaceDomainWithDelegate
  ) {
    return ReflectionFunctions.getObjectFormDeclaration(
        domainType, objectForm, movabilityType, replaceDomainWithDelegate, this::addImportAndGetSimpleName
    );
  }

  protected String buildGeneratedMethodJavadoc(String sourceClassCanonicalName, MethodStatement method) {
    return """
      /**
       * Based on the method {@link %s#%s(%s)}
       */
      """.formatted(
        addImportAndGetSimpleName(sourceClassCanonicalName),
        method.name(),
        method.params().stream()
            .map(MethodParam::type)
            .map(type -> type.actualDeclaration(this::addImportAndGetSimpleName))
            .collect(Collectors.joining(", "))
    );
  }

  protected String buildDomainLink(TypeReference type) {
    if (type.isPrimitiveReference()) {
      return "{@link " +
          ClassFunctions.wrapperClassOfPrimitive(type.asPrimitiveReferenceOrElseThrow().typename()).getSimpleName() +
          "}";
    } else if (type.isCustomTypeReference()) {
      return "{@link " +
          addImportAndGetSimpleName(type.asCustomTypeReferenceOrElseThrow().targetType().canonicalName()) + "}";
    } else {
      return "Object";
    }
  }
}
