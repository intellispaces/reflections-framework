package tech.intellispaces.reflections.framework.annotationprocessor.channel;

import java.util.Optional;

import tech.intellispaces.annotationprocessor.ArtifactValidator;
import tech.intellispaces.jstatements.customtype.CustomType;
import tech.intellispaces.reflections.framework.annotation.Ontology;
import tech.intellispaces.reflections.framework.exception.ReflectionsExceptions;

/**
 * The channel type validator.
 */
public class ChannelValidator implements ArtifactValidator {

  @Override
  public void validate(CustomType channelType) {
    validateName(channelType);
    validateEnclosingType(channelType);
    validateMethods(channelType);
  }

  private void validateName(CustomType channelType) {
    if (!channelType.simpleName().endsWith("Channel")) {
      throw ReflectionsExceptions.withMessage("Channel interface name must end with 'Channel'. Check class {0}\"",
          channelType.canonicalName());
    }
  }

  private void validateEnclosingType(CustomType channelType) {
    Optional<CustomType> enclosingType = channelType.enclosingType();
    if (enclosingType.isPresent() && !enclosingType.get().hasAnnotation(Ontology.class)) {
      throw ReflectionsExceptions.withMessage("Channel interface can only be nested to ontology interface. " +
          "Check class {0}", channelType.canonicalName());
    }
  }

  private void validateMethods(CustomType channelType) {
    int numDeclaredMethods = channelType.declaredMethods().size();
    if (numDeclaredMethods == 0) {
      throw ReflectionsExceptions.withMessage("Channel interface should contain one declared method. Check class {0}\"",
          channelType.canonicalName());
    } else if (numDeclaredMethods > 1) {
      throw ReflectionsExceptions.withMessage("Channel interface should contain one declared method only. Check class {0}\"",
          channelType.canonicalName());
    }
  }
}
