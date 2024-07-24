package tech.intellispaces.framework.core.annotation.processor.ontology;

import com.google.auto.service.AutoService;
import tech.intellispaces.framework.annotationprocessor.AnnotatedTypeProcessor;
import tech.intellispaces.framework.annotationprocessor.generator.ArtifactGenerator;
import tech.intellispaces.framework.annotationprocessor.validator.AnnotatedTypeValidator;
import tech.intellispaces.framework.core.annotation.Ontology;
import tech.intellispaces.framework.core.annotation.processor.AnnotationProcessorFunctions;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import java.util.List;
import java.util.Set;

@AutoService(Processor.class)
public class OntologyAnnotationProcessor extends AnnotatedTypeProcessor {

  public OntologyAnnotationProcessor() {
    super(Ontology.class, Set.of(ElementKind.INTERFACE));
  }

  @Override
  protected boolean isApplicable(CustomType ontologyType) {
    return AnnotationProcessorFunctions.isAutoGenerationEnabled(ontologyType);
  }

  @Override
  protected AnnotatedTypeValidator getValidator() {
    return null;
  }

  @Override
  protected List<ArtifactGenerator> makeArtifactGenerators(
      CustomType ontologyType, RoundEnvironment roundEnv
  ) {
    return AnnotationProcessorFunctions.makeOntologyArtifactGenerators(ontologyType, roundEnv);
  }
}
