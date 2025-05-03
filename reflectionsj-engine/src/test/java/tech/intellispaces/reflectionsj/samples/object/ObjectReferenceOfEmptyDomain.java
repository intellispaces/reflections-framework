package tech.intellispaces.reflectionsj.samples.object;

import tech.intellispaces.reflectionsj.object.reference.ObjectReference;
import tech.intellispaces.reflectionsj.samples.domain.EmptyDomain;

@tech.intellispaces.reflectionsj.annotation.ObjectHandle(EmptyDomain.class)
public interface ObjectReferenceOfEmptyDomain extends ObjectReference<EmptyDomain> {
}
