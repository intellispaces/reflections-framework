package tech.intellispaces.jaquarius.annotation.processor.domain;

import com.google.auto.service.AutoService;
import tech.intellispaces.jaquarius.annotation.Domain;
import tech.intellispaces.jaquarius.annotation.processor.AnnotationProcessorFunctions;
import tech.intellispaces.jaquarius.validation.DomainValidator;
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
  public List<Generator> makeGenerators(CustomType initiatorType, CustomType domainType, RoundEnvironment roundEnv) {
    return AnnotationProcessorFunctions.makeDomainArtifactGenerators(initiatorType, domainType, roundEnv);
  }
}
