package tech.intellispacesframework.core.annotation.processor;

import tech.intellispacesframework.annotationprocessor.artifact.sketch.JavaArtifactSketch;
import tech.intellispacesframework.annotationprocessor.maker.TemplateBasedJavaArtifactMaker;
import tech.intellispacesframework.commons.type.TypeFunctions;
import tech.intellispacesframework.core.exception.TraverseException;
import tech.intellispacesframework.core.guide.n1.Mover1;
import tech.intellispacesframework.core.object.ObjectFunctions;
import tech.intellispacesframework.core.space.transition.TransitionFunctions;
import tech.intellispacesframework.core.system.Modules;
import tech.intellispacesframework.core.transition.TransitionMethod1;
import tech.intellispacesframework.javastatements.statement.custom.CustomType;

import java.util.Map;

public class ObjectHandleImplMaker extends TemplateBasedJavaArtifactMaker {
  private final JavaArtifactSketch sketch = new JavaArtifactSketch();
  private String sourceClassCanonicalName;
  private String domainSimpleClassName;

  @Override
  protected String templateName() {
    return "/MovableObjectHandle.template";
  }

  protected Map<String, Object> templateVariables() {
    return Map.of(
        "SOURCE_CLASS_NAME", sourceClassCanonicalName,
        "SOURCE_CLASS_SIMPLE_NAME", TypeFunctions.getSimpleName(sourceClassCanonicalName),
        "PACKAGE_NAME", sketch.packageName(),
        "CLASS_SIMPLE_NAME", sketch.simpleName(),
        "DOMAIN_CLASS_SIMPLE_NAME", domainSimpleClassName,
        "importedClasses", sketch.getImports()
    );
  }

  @Override
  protected String canonicalName() {
    return sketch.canonicalName();
  }

  @Override
  protected boolean analyze(CustomType annotatedType) {
    sketch.canonicalName(annotatedType.canonicalName() + "Impl");

    sourceClassCanonicalName = annotatedType.canonicalName();

    CustomType domainType = ObjectFunctions.getDomainTypeOfObjectHandle(annotatedType);
    domainSimpleClassName = domainType.simpleName();


    sketch.addImport(Modules.class);
    sketch.addImport(TraverseException.class);

    sketch.addImport(Mover1.class);
    sketch.addImport(TransitionMethod1.class);
    sketch.addImport(TransitionFunctions.class);
    sketch.addImport(domainType.canonicalName());

    return true;
  }
}
