package tech.intellispaces.reflections.framework.annotationprocessor.domain;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.RoundEnvironment;

import tech.intellispaces.annotationprocessor.ArtifactGenerator;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.annotationprocessor.ArtifactProcessor;
import tech.intellispaces.annotationprocessor.ArtifactValidator;
import tech.intellispaces.commons.instance.Instances;
import tech.intellispaces.javareflection.customtype.CustomType;
import tech.intellispaces.javareflection.method.MethodStatement;
import tech.intellispaces.reflections.framework.annotation.AnnotationProcessor;
import tech.intellispaces.reflections.framework.annotation.Channel;
import tech.intellispaces.reflections.framework.annotation.Ignore;
import tech.intellispaces.reflections.framework.annotationprocessor.AnnotationFunctions;
import tech.intellispaces.reflections.framework.artifact.ArtifactTypes;

import static tech.intellispaces.javareflection.customtype.AnnotationFunctions.allAnnotationsOf;

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
            domainType, ArtifactTypes.ReflectionChannel, context.initialRoundEnvironment())
        ) {
          generators.add(new DomainChannelGenerator(domainType, method));
        }
      }
    }
    addReflectionGenerators(domainType, generators, context.initialRoundEnvironment());
    addAttachedAnnotationGenerators(domainType, generators, context);
    addObjectAssistantGenerators(domainType, generators);
    generators.add(new ReflectionAdapterGenerator(domainType));
    return generators;
  }

  private static void addAttachedAnnotationGenerators(
      CustomType annotatedType, List<ArtifactGenerator> generators, ArtifactGeneratorContext context
  ) {
    List<ArtifactProcessor> processors = allAnnotationsOf(annotatedType, AnnotationProcessor.class).stream()
        .map(AnnotationFunctions::getAnnotationProcessorClass)
        .distinct()
        .map(c -> (ArtifactProcessor) Instances.get(c))
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
  }

  private static void addObjectAssistantGenerators(
      CustomType domainType, List<ArtifactGenerator> generators
  ) {
    generators.add(new ReflectionAssistantHandleGenerator(domainType));
    generators.add(new ReflectionAssistantGenerator(domainType));
  }
}
