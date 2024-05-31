package tech.intellispacesframework.core.annotation.processor.ontology;

import com.google.auto.service.AutoService;
import tech.intellispacesframework.annotationprocessor.generator.ArtifactGenerator;
import tech.intellispacesframework.core.annotation.Ontology;
import tech.intellispacesframework.core.annotation.Transition;
import tech.intellispacesframework.core.annotation.processor.AbstractAnnotationProcessor;
import tech.intellispacesframework.javastatements.statement.custom.CustomType;
import tech.intellispacesframework.javastatements.statement.custom.MethodStatement;

import javax.annotation.processing.Processor;
import javax.lang.model.element.ElementKind;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AutoService(Processor.class)
public class OntologyAnnotationProcessor extends AbstractAnnotationProcessor {

  public OntologyAnnotationProcessor() {
    super(Ontology.class, Set.of(ElementKind.INTERFACE));
  }

  @Override
  protected boolean isApplicable(CustomType ontologyType) {
    return isAutoGenerationEnabled(ontologyType);
  }

  @Override
  protected List<ArtifactGenerator> makeArtifactGenerators(CustomType ontologyType) {
    List<ArtifactGenerator> generators = new ArrayList<>();
    for (MethodStatement method : ontologyType.declaredMethods()) {
      if (method.hasAnnotation(Transition.class)) {
        generators.add(new TransitionGenerator(ontologyType, method));
      }
    }
    return generators;
  }
}
