package tech.intellispaces.jaquarius.annotationprocessor;

import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.text.StringFunctions;
import tech.intellispaces.commons.type.Classes;
import tech.intellispaces.commons.java.reflection.AnnotatedStatement;
import tech.intellispaces.commons.java.reflection.JavaStatements;
import tech.intellispaces.commons.java.reflection.customtype.CustomType;
import tech.intellispaces.commons.java.reflection.instance.AnnotationInstance;
import tech.intellispaces.commons.java.reflection.instance.ClassInstance;
import tech.intellispaces.commons.java.reflection.instance.Instance;
import tech.intellispaces.jaquarius.annotation.AnnotationProcessor;
import tech.intellispaces.jaquarius.annotation.Preprocessing;

import javax.annotation.processing.RoundEnvironment;
import java.util.List;
import java.util.Optional;

/**
 * Common annotation processor functions.
 */
public interface AnnotationProcessorFunctions {

  static boolean isAutoGenerationEnabled(CustomType annotatedType) {
    return annotatedType.selectAnnotation(Preprocessing.class)
        .map(Preprocessing::enable).
        orElse(true);
  }

  static boolean isAutoGenerationEnabled(
      CustomType annotatedType, ArtifactTypes artifact, RoundEnvironment roundEnv
  ) {
    List<AnnotationInstance> preprocessingAnnotations = roundEnv.getElementsAnnotatedWith(Preprocessing.class).stream()
        .map(JavaStatements::statement)
        .map(stm -> (AnnotatedStatement) stm)
        .map(stm -> stm.selectAnnotation(Preprocessing.class.getCanonicalName()))
        .map(Optional::orElseThrow)
        .filter(ann -> isPreprocessingAnnotationFor(annotatedType, ann, annotatedType.canonicalName()))
        .toList();
    if (preprocessingAnnotations.isEmpty()) {
      return true;
    }
    return preprocessingAnnotations.stream().allMatch(AnnotationProcessorFunctions::isPreprocessingEnabled);
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

  static List<CustomType> findArtifactAddOns(
      CustomType customType, ArtifactTypes artifactType, RoundEnvironment roundEnv
  ) {
    return findArtifactAddOns(customType.canonicalName(), artifactType, roundEnv);
  }

  static List<CustomType> findArtifactAddOns(
      String canonicalName, ArtifactTypes artifactType, RoundEnvironment roundEnv
  ) {
    return roundEnv.getElementsAnnotatedWith(Preprocessing.class).stream()
        .map(JavaStatements::statement)
        .map(stm -> (CustomType) stm)
        .filter(stm -> isArtifactAddOnFor(stm, canonicalName, artifactType))
        .toList();
  }

  static boolean isArtifactAddOnFor(
      CustomType customType, String canonicalName, ArtifactTypes artifactType
  ) {
    return isPreprocessingAnnotationFor(
        customType,
        customType.selectAnnotation(Preprocessing.class.getCanonicalName()).orElseThrow(),
        canonicalName,
        artifactType
    );
  }

  static boolean isPreprocessingAnnotationFor(
      CustomType customType, AnnotationInstance preprocessingAnnotation, String canonicalClassName
  ) {
    CustomType preprocessingTarget = getPreprocessingAddOnTarget(customType, preprocessingAnnotation);
    return canonicalClassName.equals(preprocessingTarget.canonicalName());
  }

  static boolean isPreprocessingAnnotationFor(
      CustomType customType,
      AnnotationInstance preprocessingAnnotation,
      String canonicalClassName,
      ArtifactTypes artifact
  ) {
    CustomType preprocessingTarget = getPreprocessingAddOnTarget(customType, preprocessingAnnotation);
    if (canonicalClassName.equals(preprocessingTarget.canonicalName())) {
      return artifact.name().equals(getPreprocessingArtifactName(preprocessingAnnotation));
    }
    return false;
  }

  static CustomType getPreprocessingAddOnTarget(CustomType customType, AnnotationInstance preprocessingAnnotation) {
    if (preprocessingAnnotation.valueOf("value").isEmpty()) {
      return customType;
    }
    return preprocessingAnnotation.valueOf("value")
        .map(Instance::asClass)
        .map(Optional::orElseThrow)
        .map(ClassInstance::type)
        .orElseThrow();
  }

  static boolean isPreprocessingEnabled(AnnotationInstance preprocessingAnnotation) {
    Object enabled = preprocessingAnnotation.valueOf("enable").orElseThrow()
        .asPrimitive().orElseThrow()
        .value();
    return Boolean.TRUE == enabled;
  }

  static String getPreprocessingArtifactName(AnnotationInstance preprocessingAnnotation) {
    return preprocessingAnnotation.valueOf("artifact").orElseThrow()
        .asString().orElseThrow()
        .value();
  }
}
