package tech.intellispaces.jaquarius.annotation.processor;

import tech.intellispaces.jaquarius.annotation.Generated;
import tech.intellispaces.jaquarius.object.ObjectFunctions;
import tech.intellispaces.jaquarius.object.ObjectHandleTypes;
import tech.intellispaces.entity.type.ClassFunctions;
import tech.intellispaces.java.annotation.context.JavaArtifactContext;
import tech.intellispaces.java.annotation.generator.TemplatedGenerator;
import tech.intellispaces.java.reflection.customtype.CustomType;
import tech.intellispaces.java.reflection.method.MethodSignatureDeclarations;
import tech.intellispaces.java.reflection.method.MethodStatement;
import tech.intellispaces.java.reflection.reference.TypeReference;
import tech.intellispaces.java.reflection.method.MethodParam;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

public abstract class AbstractGenerator extends TemplatedGenerator {
  protected String generatedAnnotation;
  protected final JavaArtifactContext context = new JavaArtifactContext();

  public AbstractGenerator(CustomType initiatorType, CustomType annotatedType) {
    super(initiatorType, annotatedType);
    context.addImport(Generated.class);
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
          source = "%s",
          library = "%s",
          generator = "%s",
          date = "%s"
        )""".formatted(
          annotatedType.canonicalName(),
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
        .get(context::addImport, context::simpleNameOf);
  }

  protected String getObjectHandleDeclaration(TypeReference domainType, ObjectHandleTypes handleType) {
    return ObjectFunctions.getObjectHandleDeclaration(
        domainType, handleType, context::addToImportAndGetSimpleName
    );
  }
}
