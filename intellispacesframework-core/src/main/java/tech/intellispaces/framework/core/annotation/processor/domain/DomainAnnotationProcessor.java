package tech.intellispaces.framework.core.annotation.processor.domain;

import com.google.auto.service.AutoService;
import tech.intellispaces.framework.annotationprocessor.AnnotatedTypeProcessor;
import tech.intellispaces.framework.annotationprocessor.generator.ArtifactGenerator;
import tech.intellispaces.framework.annotationprocessor.validator.AnnotatedTypeValidator;
import tech.intellispaces.framework.core.annotation.Domain;
import tech.intellispaces.framework.core.annotation.processor.AnnotationProcessorFunctions;
import tech.intellispaces.framework.core.validation.DomainValidator;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;

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
