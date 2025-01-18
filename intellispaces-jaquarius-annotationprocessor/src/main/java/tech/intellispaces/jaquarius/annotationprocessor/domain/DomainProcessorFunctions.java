package tech.intellispaces.jaquarius.annotationprocessor.domain;

import tech.intellispaces.annotationprocessor.ArtifactGenerator;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.annotationprocessor.ArtifactProcessor;
import tech.intellispaces.annotationprocessor.ArtifactValidator;
import tech.intellispaces.general.object.Objects;
import tech.intellispaces.jaquarius.annotation.AnnotationProcessor;
import tech.intellispaces.jaquarius.annotation.Channel;
import tech.intellispaces.jaquarius.annotationprocessor.AnnotationProcessorFunctions;
import tech.intellispaces.jaquarius.annotationprocessor.ArtifactTypes;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;
import tech.intellispaces.jaquarius.space.domain.PrimaryDomains;
import tech.intellispaces.java.reflection.customtype.AnnotationFunctions;
import tech.intellispaces.java.reflection.customtype.CustomType;
import tech.intellispaces.java.reflection.method.MethodStatement;
import tech.intellispaces.java.reflection.reference.CustomTypeReference;

import javax.annotation.processing.RoundEnvironment;
import java.util.ArrayList;
import java.util.List;

public interface DomainProcessorFunctions {

  static List<ArtifactGenerator> makeDomainArtifactGenerators(
      CustomType domainType, ArtifactGeneratorContext context
  ) {
    List<ArtifactGenerator> generators = new ArrayList<>();
    for (MethodStatement method : domainType.declaredMethods()) {
      if (method.hasAnnotation(Channel.class)) {
        if (AnnotationProcessorFunctions.isAutoGenerationEnabled(
            domainType, ArtifactTypes.Channel, context.roundEnvironment())
        ) {
          generators.add(new DomainChannelGenerator(domainType, method));
        }
      }
    }
    addBasicObjectHandleGenerators(domainType, generators, context.roundEnvironment());
    addDownwardObjectHandleGenerators(domainType, generators);
    addIncludedGenerators(domainType, generators, context);
    return generators;
  }

  private static void addIncludedGenerators(
      CustomType annotatedType, List<ArtifactGenerator> generators, ArtifactGeneratorContext context
  ) {
    List<ArtifactProcessor> processors = AnnotationFunctions.allAnnotationsOf(
        annotatedType, AnnotationProcessor.class
    ).stream()
        .map(AnnotationProcessorFunctions::getAnnotationProcessorClass)
        .distinct()
        .map(c -> (ArtifactProcessor) Objects.get(c))
        .toList();
    for (ArtifactProcessor processor : processors) {
      if (processor.isApplicable(annotatedType)) {
        ArtifactValidator validator = processor.validator();
        if (validator != null) {
          validator.validate(annotatedType);
        }
        generators.addAll(processor.makeGenerators(annotatedType, context));
      }
    }
  }

  private static void addBasicObjectHandleGenerators(
      CustomType domainType, List<ArtifactGenerator> generators, RoundEnvironment roundEnv
  ) {
    if (PrimaryDomains.current().getByDomainName(NameConventionFunctions.convertJaquariusDomainName(domainType.canonicalName())) != null) {
      return;
    }

    if (
        AnnotationProcessorFunctions.isAutoGenerationEnabled(domainType, ArtifactTypes.ObjectHandle, roundEnv)
    ) {
      generators.add(new GeneralObjectHandleGenerator(domainType));
    }
    if (
        AnnotationProcessorFunctions.isAutoGenerationEnabled(domainType, ArtifactTypes.MovableObjectHandle, roundEnv)
    ) {
      generators.add(new MovableObjectHandleGenerator(domainType));
    }
    if (
        AnnotationProcessorFunctions.isAutoGenerationEnabled(domainType, ArtifactTypes.UnmovableObjectHandle, roundEnv)
    ) {
      generators.add(new UnmovableObjectHandleGenerator(domainType));
    }
  }

  private static void addDownwardObjectHandleGenerators(
      CustomType domainType, List<ArtifactGenerator> generators
  ) {
    if (PrimaryDomains.current().getByDomainName(NameConventionFunctions.convertJaquariusDomainName(domainType.canonicalName())) != null) {
      return;
    }

    List<CustomTypeReference> parents = domainType.parentTypes();
    if (parents.size() != 1) {
      return;
    }
    CustomTypeReference parentDomainType = parents.get(0);
    generators.add(new UnmovableDownwardObjectHandleGenerator(domainType, parentDomainType));
    generators.add(new MovableDownwardObjectHandleGenerator(domainType, parentDomainType));
  }
}
