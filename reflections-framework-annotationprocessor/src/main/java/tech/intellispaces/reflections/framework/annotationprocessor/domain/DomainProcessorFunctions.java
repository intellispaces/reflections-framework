package tech.intellispaces.reflections.framework.annotationprocessor.domain;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.RoundEnvironment;

import tech.intellispaces.annotationprocessor.ArtifactGenerator;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.annotationprocessor.ArtifactProcessor;
import tech.intellispaces.annotationprocessor.ArtifactValidator;
import tech.intellispaces.commons.object.Objects;
import tech.intellispaces.jstatements.customtype.CustomType;
import tech.intellispaces.jstatements.method.MethodStatement;
import tech.intellispaces.jstatements.reference.CustomTypeReference;
import tech.intellispaces.reflections.framework.annotation.AnnotationProcessor;
import tech.intellispaces.reflections.framework.annotation.Channel;
import tech.intellispaces.reflections.framework.annotation.Ignore;
import tech.intellispaces.reflections.framework.annotationprocessor.AnnotationFunctions;
import tech.intellispaces.reflections.framework.artifact.ArtifactTypes;
import tech.intellispaces.reflections.framework.naming.NameConventionFunctions;
import tech.intellispaces.reflections.framework.node.ReflectionsNodeFunctions;

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
    addReflectionGenerators(domainType, generators, context.initialRoundEnvironment());
    addDownwardReflectionGenerators(domainType, generators);
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
      generators.add(new GeneralRegularFormGenerator(domainType));
    }
    if (AnnotationFunctions.isAutoGenerationEnabled(domainType, ArtifactTypes.MovableRegularObject, roundEnv)) {
      generators.add(new MovableRegularFormGenerator(domainType));
    }
    if (AnnotationFunctions.isAutoGenerationEnabled(domainType, ArtifactTypes.UnmovableRegularObject, roundEnv)) {
      generators.add(new UnmovableRegularFormGenerator(domainType));
    }
  }

  private static void addReflectionGenerators(
      CustomType domainType, List<ArtifactGenerator> generators, RoundEnvironment roundEnv
  ) {
    if (
        AnnotationFunctions.isAutoGenerationEnabled(domainType, ArtifactTypes.Reflection, roundEnv)
    ) {
      generators.add(new GeneralReflectionTypeGenerator(domainType));
    }
    if (
        AnnotationFunctions.isAutoGenerationEnabled(domainType, ArtifactTypes.MovableReflection, roundEnv)
    ) {
      generators.add(new MovableReflectionTypeGenerator(domainType));
    }
    if (
        AnnotationFunctions.isAutoGenerationEnabled(domainType, ArtifactTypes.UnmovableReflection, roundEnv)
    ) {
      generators.add(new UnmovableReflectionTypeGenerator(domainType));
    }
  }

  private static void addDownwardReflectionGenerators(
      CustomType domainType, List<ArtifactGenerator> generators
  ) {
    if (ReflectionsNodeFunctions.ontologyReference().getDomainByName(NameConventionFunctions.convertToDomainName(domainType.canonicalName())) != null) {
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
    generators.add(new ReflectionAssistantHandleGenerator(domainType));
    generators.add(new ReflectionAssistantGenerator(domainType));
  }
}
