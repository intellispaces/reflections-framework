package tech.intellispaces.reflections.annotationprocessor.channel;

import java.util.Optional;

import tech.intellispaces.annotationprocessor.ArtifactValidator;
import tech.intellispaces.reflections.annotation.Ontology;
import tech.intellispaces.reflections.exception.JaquariusExceptions;
import tech.intellispaces.jstatements.customtype.CustomType;

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
      throw JaquariusExceptions.withMessage("Channel interface name must end with 'Channel'. Check class {0}\"",
          channelType.canonicalName());
    }
  }

  private void validateEnclosingType(CustomType channelType) {
    Optional<CustomType> enclosingType = channelType.enclosingType();
    if (enclosingType.isPresent() && !enclosingType.get().hasAnnotation(Ontology.class)) {
      throw JaquariusExceptions.withMessage("Channel interface can only be nested to ontology interface. " +
          "Check class {0}", channelType.canonicalName());
    }
  }

  private void validateMethods(CustomType channelType) {
    int numDeclaredMethods = channelType.declaredMethods().size();
    if (numDeclaredMethods == 0) {
      throw JaquariusExceptions.withMessage("Channel interface should contain one declared method. Check class {0}\"",
          channelType.canonicalName());
    } else if (numDeclaredMethods > 1) {
      throw JaquariusExceptions.withMessage("Channel interface should contain one declared method only. Check class {0}\"",
          channelType.canonicalName());
    }
  }
}
