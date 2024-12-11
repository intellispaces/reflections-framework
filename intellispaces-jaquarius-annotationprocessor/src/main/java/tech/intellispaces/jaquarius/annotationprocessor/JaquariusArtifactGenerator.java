package tech.intellispaces.jaquarius.annotationprocessor;

import tech.intellispaces.annotationprocessor.TemplatedJavaArtifactGenerator;
import tech.intellispaces.general.type.ClassFunctions;
import tech.intellispaces.jaquarius.annotation.Generated;
import tech.intellispaces.jaquarius.object.ObjectHandleFunctions;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleTypes;
import tech.intellispaces.java.reflection.customtype.CustomType;
import tech.intellispaces.java.reflection.method.MethodParam;
import tech.intellispaces.java.reflection.method.MethodSignatureDeclarations;
import tech.intellispaces.java.reflection.method.MethodStatement;
import tech.intellispaces.java.reflection.reference.TypeReference;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

public abstract class JaquariusArtifactGenerator extends TemplatedJavaArtifactGenerator {
  protected String generatedAnnotation;

  public JaquariusArtifactGenerator(CustomType sourceArtifact) {
    super(sourceArtifact);
    addImport(Generated.class);
  }

  protected String makeGeneratedAnnotation() {
    if (generatedAnnotation == null) {
      generatedAnnotation = """
        @Generated(
          source = "%s",
          library = "%s",
          generator = "%s",
          date = "%s"
        )""".formatted(
          sourceArtifact().canonicalName(),
          ClassFunctions.getJavaLibraryName(this.getClass()),
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
              .map(type -> type.actualDeclaration(this::addToImportAndGetSimpleName))
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
    return buildMethodSignature(method, method.name(), true, true, additionalParams);
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
        .get(this::addImport, this::simpleNameOf);
  }

  protected String getObjectHandleDeclaration(TypeReference domainType, ObjectHandleTypes handleType) {
    return ObjectHandleFunctions.getObjectHandleDeclaration(
        domainType, handleType, this::addToImportAndGetSimpleName
    );
  }
}
