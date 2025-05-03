package tech.intellispaces.reflectionsj.annotationprocessor.customizer;

import java.util.Optional;

import tech.intellispaces.annotationprocessor.ArtifactValidator;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.type.ClassNameFunctions;
import tech.intellispaces.reflectionsj.ArtifactType;
import tech.intellispaces.reflectionsj.annotation.AssistantCustomizer;
import tech.intellispaces.reflectionsj.annotation.Customizer;
import tech.intellispaces.reflectionsj.annotationprocessor.AnnotationFunctions;
import tech.intellispaces.reflectionsj.artifact.ArtifactTypes;
import tech.intellispaces.reflectionsj.exception.JaquariusExceptions;
import tech.intellispaces.reflectionsj.naming.NameConventionFunctions;
import tech.intellispaces.statementsj.customtype.CustomType;
import tech.intellispaces.statementsj.instance.AnnotationInstance;

/**
 * The channel type validator.
 */
public class CustomizerValidator implements ArtifactValidator {

  @Override
  public void validate(CustomType customizerType) {
    CustomType originArtifact = getOriginArtifact(customizerType);
    ArtifactType targetArtifactType = getTargetArtifactType(customizerType);
    String expectedName = NameConventionFunctions.getCustomizerCanonicalName(originArtifact, targetArtifactType);
    if (!customizerType.canonicalName().equals(expectedName)) {
      throw JaquariusExceptions.withMessage("Customizer class {0} has invalid name. For target artifact type {1} " +
              "the customizer should has name {2}",
          customizerType.canonicalName(),
          targetArtifactType.name(), ClassNameFunctions.getSimpleName(expectedName));
    }
  }

  private CustomType getOriginArtifact(CustomType customerType) {
    Optional<AnnotationInstance> annotation = customerType.selectAnnotation(AssistantCustomizer.class.getCanonicalName());
    if (annotation.isEmpty()) {
      annotation = customerType.selectAnnotation(Customizer.class.getCanonicalName());
    }

    if (annotation.isPresent()) {
      return AnnotationFunctions.getOriginArtifact(customerType, annotation.get());
    }
    throw UnexpectedExceptions.withMessage("Unable to get the customizer origin artifact. Customizer class {0}",
        customerType.canonicalName());
  }

  private ArtifactType getTargetArtifactType(CustomType customerType) {
    if (customerType.selectAnnotation(AssistantCustomizer.class.getCanonicalName()).isPresent()) {
      return ArtifactTypes.ObjectAssistant;
    }
    Optional<AnnotationInstance> annotation = customerType.selectAnnotation(Customizer.class.getCanonicalName());
    if (annotation.isPresent()) {
      return AnnotationFunctions.getTargetArtifactType(annotation.get());
    }
    throw UnexpectedExceptions.withMessage("Unable to get the customizer target artifact type. Customizer class {0}",
        customerType.canonicalName());
  }
}
