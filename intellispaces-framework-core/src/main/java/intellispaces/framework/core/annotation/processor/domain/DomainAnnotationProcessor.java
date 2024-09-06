package intellispaces.framework.core.annotation.processor.domain;

import com.google.auto.service.AutoService;
import intellispaces.common.annotationprocessor.AnnotatedTypeProcessor;
import intellispaces.common.annotationprocessor.generator.GenerationTask;
import intellispaces.common.annotationprocessor.validator.AnnotatedTypeValidator;
import intellispaces.framework.core.annotation.Domain;
import intellispaces.framework.core.annotation.processor.AnnotationProcessorFunctions;
import intellispaces.framework.core.validation.DomainValidator;
import intellispaces.common.javastatement.customtype.CustomType;

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
  public List<GenerationTask> makeTasks(CustomType initiatorType, CustomType domainType, RoundEnvironment roundEnv) {
    return AnnotationProcessorFunctions.makeDomainArtifactGenerators(initiatorType, domainType, roundEnv);
  }
}
