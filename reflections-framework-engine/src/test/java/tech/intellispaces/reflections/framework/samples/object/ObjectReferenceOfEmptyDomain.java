package tech.intellispaces.reflections.framework.samples.object;

import tech.intellispaces.reflections.framework.annotation.ObjectHandle;
import tech.intellispaces.reflections.framework.object.reference.ObjectReference;
import tech.intellispaces.reflections.framework.samples.domain.EmptyDomain;

@ObjectHandle(EmptyDomain.class)
public interface ObjectReferenceOfEmptyDomain extends ObjectReference<EmptyDomain> {
}
