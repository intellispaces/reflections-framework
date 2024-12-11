package tech.intellispaces.jaquarius.annotationprocessor.ontology;

import com.google.auto.service.AutoService;
import tech.intellispaces.annotationprocessor.ArtifactGenerator;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.annotationprocessor.ArtifactProcessor;
import tech.intellispaces.annotationprocessor.ArtifactValidator;
import tech.intellispaces.jaquarius.annotation.Ontology;
import tech.intellispaces.jaquarius.annotationprocessor.AnnotationProcessorConstants;
import tech.intellispaces.jaquarius.annotationprocessor.AnnotationProcessorFunctions;
import tech.intellispaces.java.reflection.customtype.CustomType;

import javax.annotation.processing.Processor;
import javax.lang.model.element.ElementKind;
import java.util.List;

@AutoService(Processor.class)
public class OntologyAnnotationProcessor extends ArtifactProcessor {

  public OntologyAnnotationProcessor() {
    super(ElementKind.INTERFACE, Ontology.class, AnnotationProcessorConstants.SOURCE_VERSION);
  }

  @Override
  public boolean isApplicable(CustomType ontologyType) {
    return AnnotationProcessorFunctions.isAutoGenerationEnabled(ontologyType);
  }

  @Override
  public ArtifactValidator validator() {
    return null;
  }

  @Override
  public List<ArtifactGenerator> makeGenerators(CustomType ontologyType, ArtifactGeneratorContext context) {
    return AnnotationProcessorFunctions.makeOntologyArtifactGenerators(ontologyType, context);
  }
}
