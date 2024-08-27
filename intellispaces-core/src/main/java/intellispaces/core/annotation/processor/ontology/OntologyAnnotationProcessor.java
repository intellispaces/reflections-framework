package intellispaces.core.annotation.processor.ontology;

import com.google.auto.service.AutoService;
import intellispaces.annotations.AnnotatedTypeProcessor;
import intellispaces.annotations.generator.ArtifactGenerator;
import intellispaces.annotations.validator.AnnotatedTypeValidator;
import intellispaces.core.annotation.Ontology;
import intellispaces.core.annotation.processor.AnnotationProcessorFunctions;
import intellispaces.javastatements.customtype.CustomType;

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
  public boolean isApplicable(CustomType ontologyType) {
    return AnnotationProcessorFunctions.isAutoGenerationEnabled(ontologyType);
  }

  @Override
  public AnnotatedTypeValidator getValidator() {
    return null;
  }

  @Override
  public List<ArtifactGenerator> makeArtifactGenerators(
      CustomType ontologyType, RoundEnvironment roundEnv
  ) {
    return AnnotationProcessorFunctions.makeOntologyArtifactGenerators(ontologyType, roundEnv);
  }
}
