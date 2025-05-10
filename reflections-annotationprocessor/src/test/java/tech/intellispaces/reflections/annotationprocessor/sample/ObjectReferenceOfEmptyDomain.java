package tech.intellispaces.reflections.annotationprocessor.sample;

import tech.intellispaces.reflections.framework.annotation.ObjectHandle;
import tech.intellispaces.reflections.framework.object.reference.ObjectReference;

@ObjectHandle(EmptyDomain.class)
public interface ObjectReferenceOfEmptyDomain extends ObjectReference<EmptyDomain> {
}
