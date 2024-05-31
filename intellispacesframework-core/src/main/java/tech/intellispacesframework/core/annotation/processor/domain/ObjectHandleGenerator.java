package tech.intellispacesframework.core.annotation.processor.domain;

import tech.intellispacesframework.core.object.ObjectHandle;
import tech.intellispacesframework.javastatements.statement.custom.CustomType;

import java.util.Map;

public class ObjectHandleGenerator extends AbstractObjectHandleGenerator {

  public ObjectHandleGenerator(CustomType domainType) {
    super(domainType);
  }

  @Override
  protected String templateName() {
    return "/ObjectHandle.template";
  }

  protected Map<String, Object> templateVariables() {
    return Map.of(
        "generatedAnnotation", generatedAnnotation(),
        "packageName", context.packageName(),
        "sourceClassName", sourceClassCanonicalName(),
        "sourceClassSimpleName", sourceClassSimpleName(),
        "classSimpleName", context.generatedClassSimpleName(),
        "domainTypeParamsFull", domainTypeParamsFull,
        "domainTypeParamsBrief", domainTypeParamsBrief,
        "methods", methods,
        "importedClasses", context.getImports()
    );
  }

  @Override
  protected boolean analyzeAnnotatedType() {
    context.generatedClassCanonicalName(annotatedType.canonicalName() + "Handle");

    domainTypeParamsFull = annotatedType.typeParametersFullDeclaration();
    domainTypeParamsBrief = annotatedType.typeParametersBriefDeclaration();

    analyzeObjectHandleMethods(annotatedType);

    context.addImport(ObjectHandle.class);

    return true;
  }
}
