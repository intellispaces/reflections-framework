package intellispaces.core.annotation.processor.domain;

import com.google.auto.service.AutoService;
import intellispaces.annotations.AnnotatedTypeProcessor;
import intellispaces.annotations.generator.ArtifactGenerator;
import intellispaces.annotations.validator.AnnotatedTypeValidator;
import intellispaces.core.annotation.Domain;
import intellispaces.core.annotation.processor.AnnotationProcessorFunctions;
import intellispaces.core.validation.DomainValidator;
import intellispaces.javastatements.customtype.CustomType;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import java.util.List;
import java.util.Set;

@AutoService(Processor.class)
public class DomainAnnotationProcessor extends AnnotatedTypeProcessor {

  public DomainAnnotationProcessor() {
    super(Domain.class, Set.of(ElementKind.INTERFACE));
  }

  @Override
  public boolean isApplicable(CustomType domainType) {
    return AnnotationProcessorFunctions.isAutoGenerationEnabled(domainType);
  }

  @Override
  public AnnotatedTypeValidator getValidator() {
    return new DomainValidator();
  }

  @Override
  public List<ArtifactGenerator> makeArtifactGenerators(CustomType domainType, RoundEnvironment roundEnv) {
    return AnnotationProcessorFunctions.makeDomainArtifactGenerators(domainType, roundEnv);
  }
}
