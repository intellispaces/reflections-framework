package tech.intellispaces.core.annotation.processor.domain;

import com.google.auto.service.AutoService;
import tech.intellispaces.annotations.AnnotatedTypeProcessor;
import tech.intellispaces.annotations.generator.ArtifactGenerator;
import tech.intellispaces.annotations.validator.AnnotatedTypeValidator;
import tech.intellispaces.core.annotation.Domain;
import tech.intellispaces.core.annotation.processor.AnnotationProcessorFunctions;
import tech.intellispaces.core.validation.DomainValidator;
import tech.intellispaces.javastatements.customtype.CustomType;

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
  protected boolean isApplicable(CustomType domainType) {
    return AnnotationProcessorFunctions.isAutoGenerationEnabled(domainType);
  }

  @Override
  protected AnnotatedTypeValidator getValidator() {
    return new DomainValidator();
  }

  @Override
  protected List<ArtifactGenerator> makeArtifactGenerators(CustomType domainType, RoundEnvironment roundEnv) {
    return AnnotationProcessorFunctions.makeDomainArtifactGenerators(domainType, roundEnv);
  }
}
