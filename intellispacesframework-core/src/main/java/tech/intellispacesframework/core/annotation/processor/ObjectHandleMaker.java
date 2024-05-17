package tech.intellispacesframework.core.annotation.processor;

import tech.intellispacesframework.annotationprocessor.artifact.sketch.JavaArtifactSketch;
import tech.intellispacesframework.annotationprocessor.maker.TemplateBasedJavaArtifactMaker;
import tech.intellispacesframework.commons.type.TypeFunctions;
import tech.intellispacesframework.core.object.ObjectHandle;
import tech.intellispacesframework.javastatements.statement.custom.CustomType;
import tech.intellispacesframework.javastatements.statement.custom.MethodParam;
import tech.intellispacesframework.javastatements.statement.custom.MethodStatement;
import tech.intellispacesframework.javastatements.statement.reference.TypeReference;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ObjectHandleMaker extends TemplateBasedJavaArtifactMaker {
  private final JavaArtifactSketch sketch = new JavaArtifactSketch();
  private String sourceClassCanonicalName;
  private String domainTypeParamsFull;
  private String domainTypeParamsBrief;
  private List<String> methods;

  @Override
  protected String templateName() {
    return "/ObjectHandle.template";
  }

  protected Map<String, Object> templateVariables() {
    return Map.of(
        "packageName", sketch.packageName(),
        "sourceClassName", sourceClassCanonicalName,
        "sourceClassSimpleName", TypeFunctions.getSimpleName(sourceClassCanonicalName),
        "classSimpleName", sketch.simpleName(),
        "domainTypeParamsFull", domainTypeParamsFull,
        "domainTypeParamsBrief", domainTypeParamsBrief,
        "methods", methods,
        "importedClasses", sketch.getImports()
    );
  }

  @Override
  protected String canonicalName() {
    return sketch.canonicalName();
  }

  @Override
  protected boolean analyze(CustomType domainType) {
    sourceClassCanonicalName = domainType.canonicalName();
    sketch.canonicalName(domainType.canonicalName() + "Handle");

    domainTypeParamsFull = domainType.typeParametersFullDeclaration();
    domainTypeParamsBrief = domainType.typeParametersBriefDeclaration();

    analyzeObjectHandleMethods(domainType);

    sketch.addImport(ObjectHandle.class);

    return true;
  }

  private void analyzeObjectHandleMethods(CustomType domainType) {
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
      getHandleTypeReference(domainReturnType, (simpleName, importedName) -> {
        signature.append(simpleName);
        if (importedName != null) {
          sketch.addImport(importedName);
        }
      });
      signature.append(" ");
      signature.append(method.name());
      signature.append("(");

      var firstParam = true;
      for (MethodParam param : method.params()) {
        if (!firstParam) {
          signature.append(", ");
        }
        firstParam = false;

        getHandleTypeReference(param.type(), (simpleName, importedName) -> {
          signature.append(simpleName);
          if (importedName != null) {
            sketch.addImport(importedName);
          }
        });
        signature.append(" ");
        signature.append(param.name());
      }
      signature.append(")");

      String exceptions = method.exceptions().stream()
          .map(e -> e.asCustomTypeReference().orElseThrow().targetType())
          .peek(e -> sketch.addImport(e.canonicalName()))
          .map(CustomType::simpleName)
          .collect(Collectors.joining(", "));
      if (!exceptions.isEmpty()) {
        signature.append(" throws ").append(exceptions);
      }
    }
    return signature.toString();
  }

  private void getHandleTypeReference(TypeReference type, TypeReferenceHandler handler) {
    if (type.asPrimitiveTypeReference().isPresent()) {
      handler.handle(type.asPrimitiveTypeReference().get().typename(), null);
    } else if (type.asNamedTypeReference().isPresent()) {
      handler.handle(type.asNamedTypeReference().get().name(), null);
    } else if (type.asCustomTypeReference().isPresent()) {
      CustomType targetType = type.asCustomTypeReference().get().targetType();
      if (targetType.canonicalName().startsWith("java.lang.")) {
        handler.handle(targetType.simpleName(), null);
      } else {
        handler.handle(targetType.simpleName() + "Handle", targetType.canonicalName() + "Handle");
      }
    } else {
      throw new UnsupportedOperationException("Unsupported type - " + type.referenceDeclaration());
    }
  }

  @FunctionalInterface
  private interface TypeReferenceHandler {
    void handle(String simpleName, String importedName);
  }
}
