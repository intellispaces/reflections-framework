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
        "SOURCE_CLASS_NAME", sourceClassCanonicalName,
        "SOURCE_CLASS_SIMPLE_NAME", TypeFunctions.getSimpleName(sourceClassCanonicalName),
        "PACKAGE_NAME", sketch.packageName(),
        "CLASS_SIMPLE_NAME", sketch.simpleName(),
        "DOMAIN_CLASS_SIMPLE_NAME", domainSimpleClassName,
        "CONSTRUCTORS", constructors,
        "IMPORTED_CLASSES", sketch.getImports()
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

    constructors = makeConstructors(annotatedType);

    sketch.addImport(Modules.class);
    sketch.addImport(TraverseException.class);

    sketch.addImport(Mover1.class);
    sketch.addImport(TransitionMethod1.class);
    sketch.addImport(TransitionFunctions.class);
    sketch.addImport(domainType.canonicalName());

    return true;
  }

  @SuppressWarnings("unchecked, rawtypes")
  private List<Object> makeConstructors(CustomType annotatedType) {
    List<MethodStatement> constructors;
    if (annotatedType.asClass().isPresent()) {
      constructors = annotatedType.asClass().get().constructors();
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
    return (List) constructorDescriptors;
  }
}
