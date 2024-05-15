package tech.intellispacesframework.core.annotation.processor;

import tech.intellispacesframework.annotationprocessor.artifact.sketch.JavaArtifactSketch;
import tech.intellispacesframework.annotationprocessor.maker.TemplateBasedJavaArtifactMaker;
import tech.intellispacesframework.commons.type.TypeFunctions;
import tech.intellispacesframework.core.exception.TraverseException;
import tech.intellispacesframework.core.guide.n1.Mover1;
import tech.intellispacesframework.core.object.ObjectFunctions;
import tech.intellispacesframework.core.object.ObjectHandle;
import tech.intellispacesframework.core.space.transition.TransitionFunctions;
import tech.intellispacesframework.core.system.Modules;
import tech.intellispacesframework.core.transition.TransitionMethod1;
import tech.intellispacesframework.javastatements.statement.custom.CustomType;

import java.util.Map;

public class ObjectHandleMaker extends TemplateBasedJavaArtifactMaker {
  private final JavaArtifactSketch sketch = new JavaArtifactSketch();
  private String sourceClassCanonicalName;

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
        "IMPORTED_CLASSES", sketch.getImports()
    );
  }

  @Override
  protected String canonicalName() {
    return sketch.canonicalName();
  }

  @Override
  protected boolean analyze(CustomType annotatedType) {
    sketch.canonicalName(annotatedType.canonicalName() + "Handle");

    sourceClassCanonicalName = annotatedType.canonicalName();

    sketch.addImport(ObjectHandle.class);

    return true;
  }
}
