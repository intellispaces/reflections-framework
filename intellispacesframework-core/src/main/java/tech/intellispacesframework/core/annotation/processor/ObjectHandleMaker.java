package tech.intellispacesframework.core.annotation.processor;

import tech.intellispacesframework.annotationprocessor.artifact.sketch.JavaArtifactSketch;
import tech.intellispacesframework.annotationprocessor.maker.TemplateBasedJavaArtifactMaker;
import tech.intellispacesframework.commons.type.TypeFunctions;
import tech.intellispacesframework.core.object.ObjectHandle;
import tech.intellispacesframework.javastatements.statement.custom.CustomType;

import java.util.Map;

public class ObjectHandleMaker extends TemplateBasedJavaArtifactMaker {
  private final JavaArtifactSketch sketch = new JavaArtifactSketch();
  private String sourceClassCanonicalName;
  private String domainTypeParamsFull;
  private String domainTypeParamsBrief;

  @Override
  protected String templateName() {
    return "/ObjectHandle.template";
  }

  protected Map<String, Object> templateVariables() {
    return Map.of(
        "SOURCE_CLASS_NAME", sourceClassCanonicalName,
        "SOURCE_CLASS_SIMPLE_NAME", TypeFunctions.getSimpleName(sourceClassCanonicalName),
        "PACKAGE_NAME", sketch.packageName(),
        "CLASS_SIMPLE_NAME", sketch.simpleName(),
        "DOMAIN_TYPE_PARAMS_FULL", domainTypeParamsFull,
        "DOMAIN_TYPE_PARAMS_BRIEF", domainTypeParamsBrief,
        "IMPORTED_CLASSES", sketch.getImports()
    );
  }

  @Override
  protected String canonicalName() {
    return sketch.canonicalName();
  }

  @Override
  protected boolean analyze(CustomType annotatedType) {
    sourceClassCanonicalName = annotatedType.canonicalName();
    sketch.canonicalName(annotatedType.canonicalName() + "Handle");

    domainTypeParamsFull = annotatedType.typeParametersFullDeclaration();
    domainTypeParamsBrief = annotatedType.typeParametersBriefDeclaration();

    sketch.addImport(ObjectHandle.class);

    return true;
  }
}
