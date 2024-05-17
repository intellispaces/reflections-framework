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
import tech.intellispacesframework.javastatements.statement.custom.MethodParam;
import tech.intellispacesframework.javastatements.statement.custom.MethodStatement;
import tech.intellispacesframework.javastatements.statement.reference.TypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ObjectHandleImplementationMaker extends TemplateBasedJavaArtifactMaker {
  private final JavaArtifactSketch sketch = new JavaArtifactSketch();
  private String sourceClassCanonicalName;
  private String domainSimpleClassName;
  private List<Object> constructors;

  @Override
  protected String templateName() {
    return "/MovableObjectHandleImplementation.template";
  }

  protected Map<String, Object> templateVariables() {
    return Map.of(
        "packageName", sketch.packageName(),
        "sourceClassName", sourceClassCanonicalName,
        "sourceClassSimpleName", TypeFunctions.getSimpleName(sourceClassCanonicalName),
        "classSimpleName", sketch.simpleName(),
        "domainClassSimpleName", domainSimpleClassName,
        "constructors", constructors,
        "importedClasses", sketch.getImports()
    );
  }

  @Override
  protected String canonicalName() {
    return sketch.canonicalName();
  }

  @Override
  protected boolean analyze(CustomType objectHandleType) {
    sketch.canonicalName(objectHandleType.canonicalName() + "Impl");

    sourceClassCanonicalName = objectHandleType.canonicalName();

    CustomType domainType = ObjectFunctions.getDomainTypeOfObjectHandle(objectHandleType);
    domainSimpleClassName = domainType.simpleName();

    analyzeConstructors(objectHandleType);

    sketch.addImport(Modules.class);
    sketch.addImport(TraverseException.class);

    sketch.addImport(Mover1.class);
    sketch.addImport(TransitionMethod1.class);
    sketch.addImport(TransitionFunctions.class);
    sketch.addImport(domainType.canonicalName());

    return true;
  }

  @SuppressWarnings("unchecked, rawtypes")
  private void analyzeConstructors(CustomType objectHandleType) {
    List<MethodStatement> constructors;
    if (objectHandleType.asClass().isPresent()) {
      constructors = objectHandleType.asClass().get().constructors();
    } else {
      constructors = List.of();
    }

    List<Map<String, Object>> constructorDescriptors = new ArrayList<>();
    for (MethodStatement constructor : constructors) {
      List<Map<String, String>> paramDescriptors = new ArrayList<>();
      for (MethodParam param : constructor.params()) {
        TypeReference type = param.type();
        paramDescriptors.add(Map.of(
            "name", param.name(),
            "type", type.typeBriefDeclaration()
            )
        );
        constructorDescriptors.add(Map.of("params", paramDescriptors));
        type.asCustomTypeReference().ifPresent(ctr -> sketch.addImport(ctr.targetType().canonicalName()));
      }
    }
    this.constructors = (List) constructorDescriptors;
  }
}
