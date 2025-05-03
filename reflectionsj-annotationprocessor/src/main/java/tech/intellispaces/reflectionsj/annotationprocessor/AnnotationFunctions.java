package tech.intellispaces.reflectionsj.annotationprocessor;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.processing.RoundEnvironment;

import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.text.StringFunctions;
import tech.intellispaces.commons.type.ClassFunctions;
import tech.intellispaces.commons.type.Classes;
import tech.intellispaces.reflectionsj.ArtifactType;
import tech.intellispaces.reflectionsj.annotation.AnnotationProcessor;
import tech.intellispaces.reflectionsj.annotation.ArtifactGeneration;
import tech.intellispaces.reflectionsj.annotation.AssistantCustomizer;
import tech.intellispaces.reflectionsj.annotation.Customizer;
import tech.intellispaces.reflectionsj.artifact.ArtifactTypes;
import tech.intellispaces.reflectionsj.naming.NameConventionFunctions;
import tech.intellispaces.statementsj.AnnotatedStatement;
import tech.intellispaces.statementsj.JavaStatements;
import tech.intellispaces.statementsj.customtype.CustomType;
import tech.intellispaces.statementsj.customtype.CustomTypes;
import tech.intellispaces.statementsj.instance.AnnotationInstance;
import tech.intellispaces.statementsj.instance.ClassInstance;
import tech.intellispaces.statementsj.instance.Instance;

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
      CustomType originArtifact, ArtifactType targetArtifactType, RoundEnvironment roundEnv
  ) {
    List<AnnotationInstance> preprocessingAnnotations = roundEnv.getElementsAnnotatedWith(ArtifactGeneration.class).stream()
        .map(JavaStatements::statement)
        .map(stm -> (AnnotatedStatement) stm)
        .map(astm -> astm.selectAnnotation(ArtifactGeneration.class.getCanonicalName()))
        .map(Optional::orElseThrow)
        .filter(ann -> isCustomizerFor(originArtifact, ann))
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

  static Collection<CustomType> findCustomizer(
      CustomType originArtifact, ArtifactType targetArtifactType, List<RoundEnvironment> roundEnvironments
  ) {
    return roundEnvironments.stream()
        .map(roundEnv -> findArtifactCustomizerInternal(originArtifact, targetArtifactType, roundEnv))
        .flatMap(List::stream)
        .collect(Collectors.toMap(CustomType::canonicalName, Function.identity(), (c1, c2) -> c1)).values();
  }

  static List<CustomType> findCustomizer(
      CustomType originArtifact, ArtifactType targetArtifactType, RoundEnvironment roundEnv
  ) {
    return findArtifactCustomizerInternal(originArtifact, targetArtifactType, roundEnv);
  }

  private static List<CustomType> findArtifactCustomizerInternal(
      CustomType originArtifact, ArtifactType targetArtifactType, RoundEnvironment roundEnv
  ) {
    var customizers = new HashMap<String, CustomType>();
    roundEnv.getElementsAnnotatedWith(Customizer.class).stream()
        .map(JavaStatements::statement)
        .map(stm -> (CustomType) stm)
        .filter(ct -> isCustomizerFor(ct, originArtifact, targetArtifactType))
        .forEach(ct -> customizers.put(ct.canonicalName(), ct));
    if (ArtifactTypes.ObjectAssistant == targetArtifactType) {
      roundEnv.getElementsAnnotatedWith(AssistantCustomizer.class).stream()
          .map(JavaStatements::statement)
          .map(stm -> (CustomType) stm)
          .filter(ct -> isAssistantCustomizerFor(ct, originArtifact))
          .forEach(ct -> customizers.put(ct.canonicalName(), ct));
    }

    Optional<Class<?>> customizerClass = ClassFunctions.getClass(
        NameConventionFunctions.getCustomizerCanonicalName(originArtifact, targetArtifactType)
    );
    if (customizerClass.isPresent()) {
      Class<?> aClass = customizerClass.get();
      if (aClass.isAnnotationPresent(Customizer.class)) {
        if (isCustomizerFor(CustomTypes.of(aClass), originArtifact, targetArtifactType)) {
          customizers.put(aClass.getCanonicalName(), CustomTypes.of(aClass));
        }
      }
    }
    return new ArrayList<>(customizers.values());
  }

  private static boolean isCustomizerFor(CustomType expectedOriginArtifact, AnnotationInstance annotation) {
    CustomType actualOriginArtifact = getOriginArtifact(expectedOriginArtifact, annotation);
    return expectedOriginArtifact.canonicalName().equals(actualOriginArtifact.canonicalName());
  }

  private static boolean isCustomizerFor(
      CustomType customizerType, CustomType originArtifact, ArtifactType targetArtifactType
  ) {
    return isCustomizerAnnotationFor(
        customizerType.selectAnnotation(Customizer.class.getCanonicalName()).orElseThrow(),
        customizerType,
        originArtifact,
        targetArtifactType
    );
  }

  private static boolean isAssistantCustomizerFor(CustomType customizerType, CustomType originArtifact) {
    AnnotationInstance annotation = customizerType.selectAnnotation(AssistantCustomizer.class.getCanonicalName())
        .orElseThrow();
    return annotation.valueOf("value")
        .map(Instance::asClass)
        .map(Optional::orElseThrow)
        .map(ClassInstance::type)
        .orElseThrow()
        .canonicalName().equals(originArtifact.canonicalName());
  }

  private static boolean isCustomizerAnnotationFor(
      AnnotationInstance annotation,
      CustomType customizerType,
      CustomType expectedOriginArtifact,
      ArtifactType targetArtifactType
  ) {
    CustomType actualOriginArtifact = getOriginArtifact(customizerType, annotation);
    if (expectedOriginArtifact.canonicalName().equals(actualOriginArtifact.canonicalName())) {
      return targetArtifactType.name().equals(getTargetArtifactType(annotation).name());
    }
    return false;
  }

  static CustomType getOriginArtifact(CustomType annotatedType, AnnotationInstance annotation) {
    Optional<Instance> value = annotation.valueOf("value");
    if (value.isPresent()) {
      return value
          .map(Instance::asClass)
          .map(Optional::orElseThrow)
          .map(ClassInstance::type)
          .orElseThrow();
    }
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
