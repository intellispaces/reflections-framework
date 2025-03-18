package tech.intellispaces.jaquarius.annotationprocessor.domain;

import tech.intellispaces.commons.annotation.processor.ArtifactGenerator;
import tech.intellispaces.commons.annotation.processor.ArtifactGeneratorContext;
import tech.intellispaces.commons.annotation.processor.ArtifactProcessor;
import tech.intellispaces.commons.annotation.processor.ArtifactValidator;
import tech.intellispaces.commons.object.Objects;
import tech.intellispaces.commons.reflection.customtype.AnnotationFunctions;
import tech.intellispaces.commons.reflection.customtype.CustomType;
import tech.intellispaces.commons.reflection.method.MethodStatement;
import tech.intellispaces.commons.reflection.reference.CustomTypeReference;
import tech.intellispaces.jaquarius.Jaquarius;
import tech.intellispaces.jaquarius.annotation.AnnotationProcessor;
import tech.intellispaces.jaquarius.annotation.Channel;
import tech.intellispaces.jaquarius.annotationprocessor.ArtifactGenerationAnnotationFunctions;
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
        if (ArtifactGenerationAnnotationFunctions.isAutoGenerationEnabled(
            domainType, ArtifactTypes.Channel, context.roundEnvironment())
        ) {
          generators.add(new DomainChannelGenerator(domainType, method));
        }
      }
    }
    addSimpleObjectGenerators(domainType, generators, context.roundEnvironment());
    addObjectHandleGenerators(domainType, generators, context.roundEnvironment());
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
        .map(ArtifactGenerationAnnotationFunctions::getAnnotationProcessorClass)
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
    if (ArtifactGenerationAnnotationFunctions.isAutoGenerationEnabled(domainType, ArtifactTypes.UndefinedPlainObject, roundEnv)) {
      generators.add(new UndefinedPlainObjectGenerator(domainType));
    }
    if (ArtifactGenerationAnnotationFunctions.isAutoGenerationEnabled(domainType, ArtifactTypes.MovablePlainObject, roundEnv)) {
      generators.add(new MovablePlainObjectGenerator(domainType));
    }
    if (ArtifactGenerationAnnotationFunctions.isAutoGenerationEnabled(domainType, ArtifactTypes.UnmovablePlainObject, roundEnv)) {
      generators.add(new UnmovablePlainObjectGenerator(domainType));
    }
  }

  private static void addObjectHandleGenerators(
      CustomType domainType, List<ArtifactGenerator> generators, RoundEnvironment roundEnv
  ) {
    if (
        ArtifactGenerationAnnotationFunctions.isAutoGenerationEnabled(domainType, ArtifactTypes.UndefinedObjectHandle, roundEnv)
    ) {
      generators.add(new UndefinedObjectHandleGenerator(domainType));
    }
    if (
        ArtifactGenerationAnnotationFunctions.isAutoGenerationEnabled(domainType, ArtifactTypes.MovableObjectHandle, roundEnv)
    ) {
      generators.add(new MovableObjectHandleGenerator(domainType));
    }
    if (
        ArtifactGenerationAnnotationFunctions.isAutoGenerationEnabled(domainType, ArtifactTypes.UnmovableObjectHandle, roundEnv)
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
