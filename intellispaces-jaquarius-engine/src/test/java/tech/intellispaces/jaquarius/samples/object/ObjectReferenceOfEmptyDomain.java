package tech.intellispaces.jaquarius.samples.object;

import tech.intellispaces.jaquarius.object.reference.ObjectReference;
import tech.intellispaces.jaquarius.samples.domain.EmptyDomain;

@tech.intellispaces.jaquarius.annotation.ObjectHandle(EmptyDomain.class)
public interface ObjectReferenceOfEmptyDomain extends ObjectReference<EmptyDomain> {
}
