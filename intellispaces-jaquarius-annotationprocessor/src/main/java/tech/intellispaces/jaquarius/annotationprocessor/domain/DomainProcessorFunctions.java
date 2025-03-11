package tech.intellispaces.jaquarius.annotationprocessor.domain;

import tech.intellispaces.commons.annotation.processor.ArtifactGenerator;
import tech.intellispaces.commons.annotation.processor.ArtifactGeneratorContext;
import tech.intellispaces.commons.annotation.processor.ArtifactProcessor;
import tech.intellispaces.commons.annotation.processor.ArtifactValidator;
import tech.intellispaces.commons.java.reflection.customtype.AnnotationFunctions;
import tech.intellispaces.commons.java.reflection.customtype.CustomType;
import tech.intellispaces.commons.java.reflection.method.MethodStatement;
import tech.intellispaces.commons.java.reflection.reference.CustomTypeReference;
import tech.intellispaces.commons.object.Objects;
import tech.intellispaces.jaquarius.Jaquarius;
import tech.intellispaces.jaquarius.annotation.AnnotationProcessor;
import tech.intellispaces.jaquarius.annotation.Channel;
import tech.intellispaces.jaquarius.annotationprocessor.AnnotationProcessorFunctions;
import tech.intellispaces.jaquarius.annotationprocessor.ArtifactTypes;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;

import javax.annotation.processing.RoundEnvironment;
import java.util.ArrayList;
import java.util.List;

public interface DomainProcessorFunctions {

  static List<ArtifactGenerator> makeDomainArtifactGenerators(
      CustomType domainType, ArtifactGeneratorContext context
  ) {
    var generators = new ArrayList<ArtifactGenerator>();
    for (MethodStatement method : domainType.declaredMethods()) {
      if (method.hasAnnotation(Channel.class)) {
        if (AnnotationProcessorFunctions.isAutoGenerationEnabled(
            domainType, ArtifactTypes.Channel, context.roundEnvironment())
        ) {
          generators.add(new DomainChannelGenerator(domainType, method));
        }
      }
    }
    addSimpleObjectGenerators(domainType, generators, context.roundEnvironment());
//    addObjectHandleGenerators(domainType, generators, context.roundEnvironment());
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

  private static void addSimpleObjectGenerators(
      CustomType domainType, List<ArtifactGenerator> generators, RoundEnvironment roundEnv
  ) {
    if (AnnotationProcessorFunctions.isAutoGenerationEnabled(domainType, ArtifactTypes.UndefinedSimpleObject, roundEnv)) {
      generators.add(new UndefinedSimpleObjectGenerator(domainType));
    }
    if (AnnotationProcessorFunctions.isAutoGenerationEnabled(domainType, ArtifactTypes.MovableSimpleObject, roundEnv)) {
      generators.add(new MovableSimpleObjectGenerator(domainType));
    }
    if (AnnotationProcessorFunctions.isAutoGenerationEnabled(domainType, ArtifactTypes.UnmovableSimpleObject, roundEnv)) {
      generators.add(new UnmovableSimpleObjectGenerator(domainType));
    }
  }

  private static void addObjectHandleGenerators(
      CustomType domainType, List<ArtifactGenerator> generators, RoundEnvironment roundEnv
  ) {
    if (
        AnnotationProcessorFunctions.isAutoGenerationEnabled(domainType, ArtifactTypes.UndefinedObjectHandle, roundEnv)
    ) {
      generators.add(new UndefinedObjectHandleGenerator(domainType));
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
    if (Jaquarius.settings().getKeyDomainByName(NameConventionFunctions.convertToDomainName(domainType.canonicalName())) != null) {
      return;
    }

    List<CustomTypeReference> superDomains = domainType.parentTypes();
    for (CustomTypeReference superDomain : superDomains) {
      generators.add(new UnmovableDownwardObjectGenerator(domainType, superDomain));
      generators.add(new MovableDownwardObjectGenerator(domainType, superDomain));
    }
  }
}
