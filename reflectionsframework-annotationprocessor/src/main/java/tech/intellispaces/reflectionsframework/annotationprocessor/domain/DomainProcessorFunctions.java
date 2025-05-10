package tech.intellispaces.reflectionsframework.annotationprocessor.domain;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.RoundEnvironment;

import tech.intellispaces.annotationprocessor.ArtifactGenerator;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.annotationprocessor.ArtifactProcessor;
import tech.intellispaces.annotationprocessor.ArtifactValidator;
import tech.intellispaces.commons.object.Objects;
import tech.intellispaces.reflectionsframework.Jaquarius;
import tech.intellispaces.reflectionsframework.annotation.AnnotationProcessor;
import tech.intellispaces.reflectionsframework.annotation.Channel;
import tech.intellispaces.reflectionsframework.annotation.Ignore;
import tech.intellispaces.reflectionsframework.annotationprocessor.AnnotationFunctions;
import tech.intellispaces.reflectionsframework.artifact.ArtifactTypes;
import tech.intellispaces.reflectionsframework.naming.NameConventionFunctions;
import tech.intellispaces.jstatements.customtype.CustomType;
import tech.intellispaces.jstatements.method.MethodStatement;
import tech.intellispaces.jstatements.reference.CustomTypeReference;

import static tech.intellispaces.jstatements.customtype.AnnotationFunctions.allAnnotationsOf;

public interface DomainProcessorFunctions {

  static List<ArtifactGenerator> makeDomainArtifactGenerators(
      CustomType domainType, ArtifactGeneratorContext context
  ) {
    if (domainType.selectAnnotation(Ignore.class).isPresent()) {
      return List.of();
    }

    var generators = new ArrayList<ArtifactGenerator>();
    for (MethodStatement method : domainType.declaredMethods()) {
      if (method.hasAnnotation(Channel.class)) {
        if (AnnotationFunctions.isAutoGenerationEnabled(
            domainType, ArtifactTypes.Channel, context.initialRoundEnvironment())
        ) {
          generators.add(new DomainChannelGenerator(domainType, method));
        }
      }
    }
    addSimpleObjectGenerators(domainType, generators, context.initialRoundEnvironment());
    addObjectHandleGenerators(domainType, generators, context.initialRoundEnvironment());
    addDownwardObjectHandleGenerators(domainType, generators);
    addAttachedAnnotationGenerators(domainType, generators, context);
    addObjectAssistantGenerators(domainType, generators);
    return generators;
  }

  private static void addAttachedAnnotationGenerators(
      CustomType annotatedType, List<ArtifactGenerator> generators, ArtifactGeneratorContext context
  ) {
    List<ArtifactProcessor> processors = allAnnotationsOf(annotatedType, AnnotationProcessor.class).stream()
        .map(AnnotationFunctions::getAnnotationProcessorClass)
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
    if (AnnotationFunctions.isAutoGenerationEnabled(domainType, ArtifactTypes.RegularObject, roundEnv)) {
      generators.add(new GeneralRegularObjectGenerator(domainType));
    }
    if (AnnotationFunctions.isAutoGenerationEnabled(domainType, ArtifactTypes.MovableRegularObject, roundEnv)) {
      generators.add(new MovableRegularObjectGenerator(domainType));
    }
    if (AnnotationFunctions.isAutoGenerationEnabled(domainType, ArtifactTypes.UnmovableRegularObject, roundEnv)) {
      generators.add(new UnmovableRegularObjectGenerator(domainType));
    }
  }

  private static void addObjectHandleGenerators(
      CustomType domainType, List<ArtifactGenerator> generators, RoundEnvironment roundEnv
  ) {
    if (
        AnnotationFunctions.isAutoGenerationEnabled(domainType, ArtifactTypes.ObjectHandle, roundEnv)
    ) {
      generators.add(new GeneralObjectHandleGenerator(domainType));
    }
    if (
        AnnotationFunctions.isAutoGenerationEnabled(domainType, ArtifactTypes.MovableObjectHandle, roundEnv)
    ) {
      generators.add(new MovableObjectHandleGenerator(domainType));
    }
    if (
        AnnotationFunctions.isAutoGenerationEnabled(domainType, ArtifactTypes.UnmovableObjectHandle, roundEnv)
    ) {
      generators.add(new UnmovableObjectHandleGenerator(domainType));
    }
  }

  private static void addDownwardObjectHandleGenerators(
      CustomType domainType, List<ArtifactGenerator> generators
  ) {
    if (Jaquarius.ontologyReference().getDomainByName(NameConventionFunctions.convertToDomainName(domainType.canonicalName())) != null) {
      return;
    }

    List<CustomTypeReference> superDomains = domainType.parentTypes();
    for (CustomTypeReference superDomain : superDomains) {
      generators.add(new UnmovableDownwardObjectGenerator(domainType, superDomain));
      generators.add(new MovableDownwardObjectGenerator(domainType, superDomain));
    }
  }

  private static void addObjectAssistantGenerators(
      CustomType domainType, List<ArtifactGenerator> generators
  ) {
    generators.add(new ObjectAssistantBrokerGenerator(domainType));
    generators.add(new ObjectAssistantGenerator(domainType));
  }
}
