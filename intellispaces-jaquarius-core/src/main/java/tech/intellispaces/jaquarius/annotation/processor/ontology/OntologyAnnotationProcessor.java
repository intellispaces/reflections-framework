package tech.intellispaces.jaquarius.annotation.processor.ontology;

import com.google.auto.service.AutoService;
import tech.intellispaces.jaquarius.annotation.Ontology;
import tech.intellispaces.jaquarius.annotation.processor.AnnotationProcessorFunctions;
import tech.intellispaces.java.annotation.AnnotatedTypeProcessor;
import tech.intellispaces.java.annotation.generator.Generator;
import tech.intellispaces.java.annotation.validator.AnnotatedTypeValidator;
import tech.intellispaces.java.reflection.customtype.CustomType;

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
  public List<Generator> makeGenerators(CustomType initiatorType, CustomType ontologyType, RoundEnvironment roundEnv) {
    return AnnotationProcessorFunctions.makeOntologyArtifactGenerators(initiatorType, ontologyType, roundEnv);
  }
}
