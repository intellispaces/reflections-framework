package intellispaces.jaquarius.samples.object;

import intellispaces.jaquarius.object.ObjectHandle;
import intellispaces.jaquarius.samples.domain.DomainEmpty;

@intellispaces.jaquarius.annotation.ObjectHandle(DomainEmpty.class)
public interface ObjectHandleOfDomainEmpty extends ObjectHandle<DomainEmpty> {
}
