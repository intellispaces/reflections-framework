package tech.intellispaces.reflectionsj.annotationprocessor.object.handle;

import java.util.List;

import tech.intellispaces.annotationprocessor.ArtifactGenerator;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.reflectionsj.object.reference.ObjectReferenceFunctions;
import tech.intellispaces.statementsj.customtype.CustomType;

public interface ObjectHandleProcessorFunctions {

  static List<ArtifactGenerator> makeObjectHandleArtifactGenerators(
      CustomType objectHandleType
  ) {
    if (ObjectReferenceFunctions.isUnmovableObjectHandle(objectHandleType)) {
      return List.of(new UnmovableObjectWrapperGenerator(objectHandleType));
    } else if (ObjectReferenceFunctions.isMovableObjectHandle(objectHandleType)) {
      return List.of(new MovableObjectWrapperGenerator(objectHandleType));
    } else {
      throw UnexpectedExceptions.withMessage("Could not define movable type of the object handle {0}",
          objectHandleType.canonicalName());
    }
  }
}
