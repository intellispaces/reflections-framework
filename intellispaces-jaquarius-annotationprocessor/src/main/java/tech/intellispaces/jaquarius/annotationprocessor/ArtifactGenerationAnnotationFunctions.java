package tech.intellispaces.jaquarius.annotationprocessor;

import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.java.reflection.AnnotatedStatement;
import tech.intellispaces.commons.java.reflection.JavaStatements;
import tech.intellispaces.commons.java.reflection.customtype.CustomType;
import tech.intellispaces.commons.java.reflection.instance.AnnotationInstance;
import tech.intellispaces.commons.java.reflection.instance.ClassInstance;
import tech.intellispaces.commons.java.reflection.instance.Instance;
import tech.intellispaces.commons.text.StringFunctions;
import tech.intellispaces.commons.type.Classes;
import tech.intellispaces.jaquarius.annotation.AnnotationProcessor;
import tech.intellispaces.jaquarius.annotation.ArtifactCustomizer;
import tech.intellispaces.jaquarius.annotation.ArtifactGeneration;

import javax.annotation.processing.RoundEnvironment;
import java.util.List;
import java.util.Optional;

/**
 * Functions related to artifact generation annotations.
 */
public interface ArtifactGenerationAnnotationFunctions {

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
    return preprocessingAnnotations.stream().allMatch(ArtifactGenerationAnnotationFunctions::isArtifactGenerationEnabled);
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

  static List<CustomType> findArtifactCustomizers(
      CustomType customType, ArtifactTypes targetArtifactType, RoundEnvironment roundEnv
  ) {
    return findArtifactCustomizers(customType.canonicalName(), targetArtifactType, roundEnv);
  }

  static List<CustomType> findArtifactCustomizers(
      String canonicalName, ArtifactTypes targetArtifactType, RoundEnvironment roundEnv
  ) {
    return roundEnv.getElementsAnnotatedWith(ArtifactCustomizer.class).stream()
        .map(JavaStatements::statement)
        .map(stm -> (CustomType) stm)
        .filter(stm -> isArtifactCustomizerFor(stm, canonicalName, targetArtifactType))
        .toList();
  }

  static boolean isArtifactCustomizerFor(
      CustomType customType, String canonicalName, ArtifactTypes targetArtifactType
  ) {
    return isAnnotationFor(
        customType,
        customType.selectAnnotation(ArtifactCustomizer.class.getCanonicalName()).orElseThrow(),
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
      ArtifactTypes targetArtifactType
  ) {
    CustomType originArtifact = getOriginArtifact(annotatedType, annotation);
    if (canonicalClassName.equals(originArtifact.canonicalName())) {
      return targetArtifactType.name().equals(getTargetArtifactType(annotation));
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

  static String getTargetArtifactType(AnnotationInstance annotation) {
    return annotation.valueOf("target").orElseThrow()
        .asString().orElseThrow()
        .value();
  }

  static boolean isArtifactGenerationEnabled(AnnotationInstance artifactGenerationAnnotation) {
    Object enabled = artifactGenerationAnnotation.valueOf("enable").orElseThrow()
        .asPrimitive().orElseThrow()
        .value();
    return (Boolean.TRUE == enabled);
  }
}
