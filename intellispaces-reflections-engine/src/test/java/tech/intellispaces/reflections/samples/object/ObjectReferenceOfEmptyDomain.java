package tech.intellispaces.reflections.samples.object;

import tech.intellispaces.reflections.object.reference.ObjectReference;
import tech.intellispaces.reflections.samples.domain.EmptyDomain;

@tech.intellispaces.reflections.annotation.ObjectHandle(EmptyDomain.class)
public interface ObjectReferenceOfEmptyDomain extends ObjectReference<EmptyDomain> {
}
