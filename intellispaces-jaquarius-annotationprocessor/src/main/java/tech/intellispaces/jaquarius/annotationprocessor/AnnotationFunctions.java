package tech.intellispaces.jaquarius.annotationprocessor;

import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.reflection.AnnotatedStatement;
import tech.intellispaces.commons.reflection.JavaStatements;
import tech.intellispaces.commons.reflection.customtype.CustomType;
import tech.intellispaces.commons.reflection.instance.AnnotationInstance;
import tech.intellispaces.commons.reflection.instance.ClassInstance;
import tech.intellispaces.commons.reflection.instance.Instance;
import tech.intellispaces.commons.text.StringFunctions;
import tech.intellispaces.commons.type.Classes;
import tech.intellispaces.jaquarius.ArtifactType;
import tech.intellispaces.jaquarius.annotation.AnnotationProcessor;
import tech.intellispaces.jaquarius.annotation.ArtifactExtension;
import tech.intellispaces.jaquarius.annotation.ArtifactGeneration;
import tech.intellispaces.jaquarius.artifact.ArtifactTypes;

import javax.annotation.processing.RoundEnvironment;
import java.lang.annotation.Annotation;
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
      CustomType annotatedType, ArtifactTypes targetArtifactType, RoundEnvironment roundEnv
  ) {
    List<AnnotationInstance> preprocessingAnnotations = roundEnv.getElementsAnnotatedWith(ArtifactGeneration.class).stream()
        .map(JavaStatements::statement)
        .map(stm -> (AnnotatedStatement) stm)
        .map(stm -> stm.selectAnnotation(ArtifactGeneration.class.getCanonicalName()))
        .map(Optional::orElseThrow)
        .filter(ann -> isAnnotationFor(annotatedType, ann, annotatedType.canonicalName()))
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
      CustomType customType, ArtifactType targetArtifactType, List<RoundEnvironment> roundEnvironments
  ) {
    return roundEnvironments.stream()
        .map(roundEnv -> findArtifactExtensions(customType.canonicalName(), targetArtifactType, roundEnv))
        .flatMap(List::stream)
        .collect(Collectors.toMap(CustomType::canonicalName, Function.identity(), (c1, c2) -> c1)).values();
  }

  static List<CustomType> findArtifactExtensions(
      CustomType customType, ArtifactType targetArtifactType, RoundEnvironment roundEnv
  ) {
    return findArtifactExtensions(customType.canonicalName(), targetArtifactType, roundEnv);
  }

  static List<CustomType> findArtifactExtensions(
      String canonicalName, ArtifactType targetArtifactType, RoundEnvironment roundEnv
  ) {
    return roundEnv.getElementsAnnotatedWith(ArtifactExtension.class).stream()
        .map(JavaStatements::statement)
        .map(stm -> (CustomType) stm)
        .filter(stm -> isExtensionFor(stm, canonicalName, targetArtifactType))
        .toList();
  }

  private static boolean isExtensionFor(
      CustomType customType, String canonicalName, ArtifactType targetArtifactType
  ) {
    return isAnnotationFor(
        customType,
        customType.selectAnnotation(ArtifactExtension.class.getCanonicalName()).orElseThrow(),
        canonicalName,
        targetArtifactType
    );
  }

  static boolean isAnnotationFor(
      CustomType customType, AnnotationInstance annotation, String canonicalClassName
  ) {
    CustomType originArtifact = getOriginArtifact(customType, annotation);
    return canonicalClassName.equals(originArtifact.canonicalName());
  }

  static boolean isAnnotationFor(
      CustomType annotatedType,
      AnnotationInstance annotation,
      String canonicalClassName,
      ArtifactType targetArtifactType
  ) {
    CustomType originArtifact = getOriginArtifact(annotatedType, annotation);
    if (canonicalClassName.equals(originArtifact.canonicalName())) {
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

  static ArtifactType getTargetArtifactType(AnnotationInstance annotation) {
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
