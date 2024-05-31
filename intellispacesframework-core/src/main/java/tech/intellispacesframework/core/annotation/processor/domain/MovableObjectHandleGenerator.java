package tech.intellispacesframework.core.annotation.processor.domain;

import tech.intellispacesframework.commons.type.TypeFunctions;
import tech.intellispacesframework.core.object.MovableObjectHandle;
import tech.intellispacesframework.javastatements.statement.custom.CustomType;

import java.util.Map;

public class MovableObjectHandleGenerator extends AbstractObjectHandleGenerator {

  public MovableObjectHandleGenerator(CustomType domainType) {
    super(domainType);
  }

  @Override
  protected String templateName() {
    return "/MovableObjectHandle.template";
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
    context.generatedClassCanonicalName(TypeFunctions.addPrefixToClassName(
        "Movable", annotatedType.canonicalName()) + "Handle");

    domainTypeParamsFull = annotatedType.typeParametersFullDeclaration();
    domainTypeParamsBrief = annotatedType.typeParametersBriefDeclaration();

    analyzeObjectHandleMethods(annotatedType);

    context.addImport(MovableObjectHandle.class);

    return true;
  }
}
