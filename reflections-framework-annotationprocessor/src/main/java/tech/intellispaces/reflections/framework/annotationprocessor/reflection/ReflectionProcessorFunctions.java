package tech.intellispaces.reflections.framework.annotationprocessor.reflection;

import java.util.List;

import tech.intellispaces.annotationprocessor.ArtifactGenerator;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.reflections.framework.reflection.ReflectionFunctions;
import tech.intellispaces.jstatements.customtype.CustomType;

public interface ReflectionProcessorFunctions {

  static List<ArtifactGenerator> makeReflectionArtifactGenerators(
      CustomType objectHandleType
  ) {
    if (ReflectionFunctions.isUnmovableObjectHandle(objectHandleType)) {
      return List.of(new UnmovableReflectionWrapperGenerator(objectHandleType));
    } else if (ReflectionFunctions.isMovableObjectHandle(objectHandleType)) {
      return List.of(new MovableReflectionWrapperGenerator(objectHandleType));
    } else {
      throw UnexpectedExceptions.withMessage("Could not define movable type of the object reflection {0}",
          objectHandleType.canonicalName());
    }
  }
}
