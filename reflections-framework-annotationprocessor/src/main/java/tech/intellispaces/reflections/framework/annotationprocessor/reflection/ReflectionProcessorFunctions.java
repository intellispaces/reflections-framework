package tech.intellispaces.reflections.framework.annotationprocessor.reflection;

import java.util.List;

import tech.intellispaces.annotationprocessor.ArtifactGenerator;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.jstatements.customtype.CustomType;
import tech.intellispaces.reflections.framework.reflection.ReflectionFunctions;

public interface ReflectionProcessorFunctions {

  static List<ArtifactGenerator> makeReflectionArtifactGenerators(CustomType reflectionType) {
    if (ReflectionFunctions.isUnmovableReflection(reflectionType)) {
      return List.of(new UnmovableReflectionWrapperGenerator(reflectionType));
    } else if (ReflectionFunctions.isMovableReflection(reflectionType)) {
      return List.of(new MovableReflectionWrapperGenerator(reflectionType));
    } else {
      throw UnexpectedExceptions.withMessage("Could not define movable type of the object reflection {0}",
          reflectionType.canonicalName());
    }
  }
}
