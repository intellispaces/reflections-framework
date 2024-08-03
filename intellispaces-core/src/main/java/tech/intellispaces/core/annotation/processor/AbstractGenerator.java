package tech.intellispaces.core.annotation.processor;

import tech.intellispaces.annotations.artifact.JavaArtifactContext;
import tech.intellispaces.annotations.generator.TemplateBasedJavaArtifactGenerator;
import tech.intellispaces.commons.type.TypeFunctions;
import tech.intellispaces.core.object.ObjectFunctions;
import tech.intellispaces.core.object.ObjectHandleTypes;
import tech.intellispaces.javastatements.customtype.CustomType;
import tech.intellispaces.javastatements.method.MethodParam;
import tech.intellispaces.javastatements.method.MethodSignatureDeclarations;
import tech.intellispaces.javastatements.method.MethodStatement;
import tech.intellispaces.javastatements.reference.TypeReference;

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
    return ObjectFunctions.getObjectHandleDeclaration(
        domainType, handleType, context::addToImportAndGetSimpleName
    );
  }
}
