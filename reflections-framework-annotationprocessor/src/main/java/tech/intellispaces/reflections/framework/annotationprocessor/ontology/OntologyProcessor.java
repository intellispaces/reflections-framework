package tech.intellispaces.reflections.framework.annotationprocessor.ontology;

import java.util.List;
import javax.annotation.processing.Processor;
import javax.lang.model.element.ElementKind;

import com.google.auto.service.AutoService;

import tech.intellispaces.annotationprocessor.ArtifactGenerator;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.annotationprocessor.ArtifactProcessor;
import tech.intellispaces.annotationprocessor.ArtifactValidator;
import tech.intellispaces.javareflection.customtype.CustomType;
import tech.intellispaces.reflections.framework.annotation.Ontology;
import tech.intellispaces.reflections.framework.annotationprocessor.AnnotationFunctions;
import tech.intellispaces.reflections.framework.annotationprocessor.ReflectionsArtifactProcessor;

@AutoService(Processor.class)
public class OntologyProcessor extends ArtifactProcessor {

  public OntologyProcessor() {
    super(ElementKind.INTERFACE, Ontology.class, ReflectionsArtifactProcessor.SOURCE_VERSION);
  }

  @Override
  public boolean isApplicable(CustomType ontologyType) {
    return AnnotationFunctions.isAutoGenerationEnabled(ontologyType);
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
