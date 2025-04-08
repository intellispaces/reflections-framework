package tech.intellispaces.jaquarius.annotationprocessor;

import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.text.StringFunctions;
import tech.intellispaces.commons.type.Classes;
import tech.intellispaces.jaquarius.ArtifactType;
import tech.intellispaces.jaquarius.annotation.AnnotationProcessor;
import tech.intellispaces.jaquarius.annotation.ArtifactGeneration;
import tech.intellispaces.jaquarius.annotation.AssistantExtension;
import tech.intellispaces.jaquarius.annotation.Extension;
import tech.intellispaces.jaquarius.artifact.ArtifactTypes;
import tech.intellispaces.reflection.AnnotatedStatement;
import tech.intellispaces.reflection.JavaStatements;
import tech.intellispaces.reflection.customtype.CustomType;
import tech.intellispaces.reflection.instance.AnnotationInstance;
import tech.intellispaces.reflection.instance.ClassInstance;
import tech.intellispaces.reflection.instance.Instance;

import javax.annotation.processing.RoundEnvironment;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Functions related to artifact generation annotations.
 */
public interface AnnotationFunctions {

  static boolean isAutoGenerationEnabled(CustomType annotatedType) {
    return annotatedType.selectAnnotation(ArtifactGeneration.class)
        .map(ArtifactGeneration::enable).
        orElse(true);
  }

  static boolean isAutoGenerationEnabled(
      CustomType sourceArtifact, ArtifactType targetArtifactType, RoundEnvironment roundEnv
  ) {
    List<AnnotationInstance> preprocessingAnnotations = roundEnv.getElementsAnnotatedWith(ArtifactGeneration.class).stream()
        .map(JavaStatements::statement)
        .map(stm -> (AnnotatedStatement) stm)
        .map(astm -> astm.selectAnnotation(ArtifactGeneration.class.getCanonicalName()))
        .map(Optional::orElseThrow)
        .filter(ann -> isExtensionFor(sourceArtifact, ann))
        .toList();
    if (preprocessingAnnotations.isEmpty()) {
      return true;
    }
    return preprocessingAnnotations.stream().allMatch(AnnotationFunctions::isArtifactGenerationEnabled);
  }

  static Class<?> getAnnotationProcessorClass(AnnotationProcessor annotationProcessor) {
    if (annotationProcessor.value() != Void.class) {
      return annotationProcessor.value();
    }
    if (StringFunctions.isNotBlank(annotationProcessor.name())) {
      String className = annotationProcessor.name().trim();
      return Classes.get(className).orElseThrow(() -> UnexpectedExceptions.withMessage("Could not find class {0}", className));
    }
    throw UnexpectedExceptions.withMessage("Invalid usage of annotation {0}", AnnotationProcessor.class.getSimpleName());
  }

  static Collection<CustomType> findArtifactExtensions(
      CustomType sourceArtifact, ArtifactType targetArtifactType, List<RoundEnvironment> roundEnvironments
  ) {
    return roundEnvironments.stream()
        .map(roundEnv -> findArtifactExtensionInternal(sourceArtifact, targetArtifactType, roundEnv))
        .flatMap(List::stream)
        .collect(Collectors.toMap(CustomType::canonicalName, Function.identity(), (c1, c2) -> c1)).values();
  }

  static List<CustomType> findArtifactExtensions(
      CustomType sourceArtifact, ArtifactType targetArtifactType, RoundEnvironment roundEnv
  ) {
    return findArtifactExtensionInternal(sourceArtifact, targetArtifactType, roundEnv);
  }

  private static List<CustomType> findArtifactExtensionInternal(
      CustomType sourceArtifact, ArtifactType targetArtifactType, RoundEnvironment roundEnv
  ) {
    var extensions = new ArrayList<CustomType>();
    roundEnv.getElementsAnnotatedWith(Extension.class).stream()
        .map(JavaStatements::statement)
        .map(stm -> (CustomType) stm)
        .filter(ct -> isExtensionFor(ct, sourceArtifact, targetArtifactType))
        .forEach(extensions::add);
    if (ArtifactTypes.ObjectAssistant == targetArtifactType) {
      roundEnv.getElementsAnnotatedWith(AssistantExtension.class).stream()
          .map(JavaStatements::statement)
          .map(stm -> (CustomType) stm)
          .filter(ct -> isAssistantExtensionFor(ct, sourceArtifact))
          .forEach(extensions::add);
    }
    return extensions;
  }

  private static boolean isExtensionFor(CustomType sourceArtifact, AnnotationInstance annotation) {
    CustomType originArtifact = getOriginArtifact(sourceArtifact, annotation);
    return sourceArtifact.canonicalName().equals(originArtifact.canonicalName());
  }

  private static boolean isExtensionFor(
      CustomType extensionType, CustomType sourceArtifact, ArtifactType targetArtifactType
  ) {
    return isExtensionAnnotationFor(
        extensionType.selectAnnotation(Extension.class.getCanonicalName()).orElseThrow(),
        extensionType,
        sourceArtifact,
        targetArtifactType
    );
  }

  private static boolean isAssistantExtensionFor(CustomType extensionType, CustomType sourceArtifact) {
    AnnotationInstance annotation = extensionType.selectAnnotation(AssistantExtension.class.getCanonicalName())
        .orElseThrow();
    return annotation.valueOf("value")
        .map(Instance::asClass)
        .map(Optional::orElseThrow)
        .map(ClassInstance::type)
        .orElseThrow()
        .canonicalName().equals(sourceArtifact.canonicalName());
  }

  private static boolean isExtensionAnnotationFor(
      AnnotationInstance annotation,
      CustomType extensionType,
      CustomType sourceArtifact,
      ArtifactType targetArtifactType
  ) {
    CustomType originArtifact = getOriginArtifact(extensionType, annotation);
    if (sourceArtifact.canonicalName().equals(originArtifact.canonicalName())) {
      return targetArtifactType.name().equals(getTargetArtifactType(annotation).name());
    }
    return false;
  }

  static CustomType getOriginArtifact(CustomType annotatedType, AnnotationInstance annotation) {
    if (annotation.valueOf("origin").isEmpty()) {
      return annotatedType;
    }
    return annotation.valueOf("origin")
        .map(Instance::asClass)
        .map(Optional::orElseThrow)
        .map(ClassInstance::type)
        .orElseThrow();
  }

  private static ArtifactType getTargetArtifactType(AnnotationInstance annotation) {
    return ArtifactTypes.valueOf(
        annotation.valueOf("target").orElseThrow()
        .asEnum().orElseThrow()
        .name()
    );
  }

  static boolean isArtifactGenerationEnabled(AnnotationInstance artifactGenerationAnnotation) {
    Object enabled = artifactGenerationAnnotation.valueOf("enable").orElseThrow()
        .asPrimitive().orElseThrow()
        .value();
    return (Boolean.TRUE == enabled);
  }

  static boolean isAssignableAnnotation(AnnotatedStatement type, Class<? extends Annotation> annotationClass) {
    return isAssignableAnnotation(type, annotationClass, new HashSet<>());
  }

  private static boolean isAssignableAnnotation(
      AnnotatedStatement type, Class<? extends Annotation> annotationClass, Set<String> history
  ) {
    if (!history.add(((CustomType) type).canonicalName())) {
      return false;
    }

    if (type.hasAnnotation(annotationClass)) {
      return true;
    }

    for (AnnotationInstance annotation : type.annotations()) {
      if (isAssignableAnnotation(annotation.annotationStatement(), annotationClass, history)) {
        return true;
      }
    }
    return false;
  }
}
