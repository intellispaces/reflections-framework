package intellispaces.framework.core.annotation.processor;

import intellispaces.common.annotationprocessor.context.JavaArtifactContext;
import intellispaces.common.annotationprocessor.generator.TemplateSourceArtifactGenerationTask;
import intellispaces.common.base.type.TypeFunctions;
import intellispaces.framework.core.annotation.Generated;
import intellispaces.framework.core.object.ObjectFunctions;
import intellispaces.framework.core.object.ObjectHandleTypes;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.common.javastatement.method.MethodParam;
import intellispaces.common.javastatement.method.MethodSignatureDeclarations;
import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.common.javastatement.reference.TypeReference;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

public abstract class AbstractGenerationTask extends TemplateSourceArtifactGenerationTask {
  protected String generatedAnnotation;
  protected final JavaArtifactContext context = new JavaArtifactContext();

  public AbstractGenerationTask(CustomType initiatorType, CustomType annotatedType) {
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
