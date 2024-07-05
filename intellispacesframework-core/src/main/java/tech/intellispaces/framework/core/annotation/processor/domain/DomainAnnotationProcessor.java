package tech.intellispaces.framework.core.annotation.processor.domain;

import com.google.auto.service.AutoService;
import tech.intellispaces.framework.annotationprocessor.AnnotatedTypeValidator;
import tech.intellispaces.framework.annotationprocessor.generator.ArtifactGenerator;
import tech.intellispaces.framework.core.annotation.Domain;
import tech.intellispaces.framework.core.annotation.Transition;
import tech.intellispaces.framework.core.annotation.processor.AbstractAnnotationProcessor;
import tech.intellispaces.framework.core.validation.DomainValidator;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;
import tech.intellispaces.framework.javastatements.statement.method.MethodStatement;

import javax.annotation.processing.Processor;
import javax.lang.model.element.ElementKind;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AutoService(Processor.class)
public class DomainAnnotationProcessor extends AbstractAnnotationProcessor {

  public DomainAnnotationProcessor() {
    super(Domain.class, Set.of(ElementKind.INTERFACE));
  }

  @Override
  protected boolean isApplicable(CustomType domainType) {
    return isAutoGenerationEnabled(domainType);
  }

  @Override
  protected AnnotatedTypeValidator getValidator() {
    return new DomainValidator();
  }

  @Override
  protected List<ArtifactGenerator> makeArtifactGenerators(CustomType domainType) {
    List<ArtifactGenerator> generators = new ArrayList<>();
    for (MethodStatement method : domainType.declaredMethods()) {
      if (method.hasAnnotation(Transition.class)) {
        if (isAutoGenerationEnabled(domainType, "Transition")) {
          generators.add(new DomainTransitionGenerator(domainType, method));
        }
        if (isAutoGenerationEnabled(domainType, "Guide")) {
          generators.add(new DomainGuideGenerator(domainType, method));
        }
      }
    }
    if (isAutoGenerationEnabled(domainType, "ObjectHandle")) {
      generators.add(new CommonObjectHandleGenerator(domainType));
    }
    if (isAutoGenerationEnabled(domainType, "MovableObjectHandle")) {
      generators.add(new MovableObjectHandleGenerator(domainType));
    }
    if (isAutoGenerationEnabled(domainType, "UnmovableObjectHandle")) {
      generators.add(new UnmovableObjectHandleGenerator(domainType));
    }
    return generators;
  }
}
