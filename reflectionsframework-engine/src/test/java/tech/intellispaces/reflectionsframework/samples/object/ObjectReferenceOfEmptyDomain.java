package tech.intellispaces.reflectionsframework.samples.object;

import tech.intellispaces.reflectionsframework.object.reference.ObjectReference;
import tech.intellispaces.reflectionsframework.samples.domain.EmptyDomain;

@tech.intellispaces.reflectionsframework.annotation.ObjectHandle(EmptyDomain.class)
public interface ObjectReferenceOfEmptyDomain extends ObjectReference<EmptyDomain> {
}
