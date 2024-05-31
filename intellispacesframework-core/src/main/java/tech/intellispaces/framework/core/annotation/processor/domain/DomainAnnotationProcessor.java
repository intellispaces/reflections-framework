package tech.intellispaces.framework.core.annotation.processor.domain;

import com.google.auto.service.AutoService;
import tech.intellispaces.framework.core.annotation.Domain;
import tech.intellispaces.framework.core.annotation.Transition;
import tech.intellispaces.framework.core.annotation.processor.AbstractAnnotationProcessor;
import tech.intellispaces.framework.annotationprocessor.generator.ArtifactGenerator;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;

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
  protected List<ArtifactGenerator> makeArtifactGenerators(CustomType domainType) {
    List<ArtifactGenerator> generators = new ArrayList<>();
    domainType.declaredMethods().stream()
        .filter(m -> m.hasAnnotation(Transition.class))
        .map(m -> new TransitionGenerator(domainType, m))
        .forEach(generators::add);
    if (isAutoGenerationEnabled(domainType, "ObjectHandle")) {
      generators.add(new ObjectHandleGenerator(domainType));
    }
    if (isAutoGenerationEnabled(domainType, "MovableObjectHandle")) {
      generators.add(new MovableObjectHandleGenerator(domainType));
    }
    return generators;
  }
}
