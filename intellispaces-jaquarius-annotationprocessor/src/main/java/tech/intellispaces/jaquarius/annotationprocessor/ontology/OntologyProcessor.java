package tech.intellispaces.jaquarius.annotationprocessor.ontology;

import com.google.auto.service.AutoService;
import tech.intellispaces.commons.annotation.processor.ArtifactGenerator;
import tech.intellispaces.commons.annotation.processor.ArtifactGeneratorContext;
import tech.intellispaces.commons.annotation.processor.ArtifactProcessor;
import tech.intellispaces.commons.annotation.processor.ArtifactValidator;
import tech.intellispaces.commons.reflection.customtype.CustomType;
import tech.intellispaces.jaquarius.annotation.Ontology;
import tech.intellispaces.jaquarius.annotationprocessor.ArtifactGenerationAnnotationFunctions;
import tech.intellispaces.jaquarius.annotationprocessor.JaquariusArtifactProcessor;

import javax.annotation.processing.Processor;
import javax.lang.model.element.ElementKind;
import java.util.List;

@AutoService(Processor.class)
public class OntologyProcessor extends ArtifactProcessor {

  public OntologyProcessor() {
    super(ElementKind.INTERFACE, Ontology.class, JaquariusArtifactProcessor.SOURCE_VERSION);
  }

  @Override
  public boolean isApplicable(CustomType ontologyType) {
    return ArtifactGenerationAnnotationFunctions.isAutoGenerationEnabled(ontologyType);
  }

  @Override
  public ArtifactValidator validator() {
    return null;
  }

  @Override
  public List<ArtifactGenerator> makeGenerators(CustomType ontologyType, ArtifactGeneratorContext context) {
    return OntologyProcessorFunctions.makeOntologyArtifactGenerators(ontologyType, context);
  }
}
