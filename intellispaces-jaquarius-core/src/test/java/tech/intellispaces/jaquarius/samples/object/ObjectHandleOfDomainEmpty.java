package tech.intellispaces.jaquarius.samples.object;

import tech.intellispaces.jaquarius.object.ObjectHandle;
import tech.intellispaces.jaquarius.samples.domain.DomainEmpty;

@tech.intellispaces.jaquarius.annotation.ObjectHandle(DomainEmpty.class)
public interface ObjectHandleOfDomainEmpty extends ObjectHandle<DomainEmpty> {
}
