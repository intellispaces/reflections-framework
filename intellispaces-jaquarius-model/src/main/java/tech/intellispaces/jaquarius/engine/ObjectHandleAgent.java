package tech.intellispaces.jaquarius.engine;

import tech.intellispaces.action.Action;
import tech.intellispaces.jaquarius.engine.descriptor.ObjectHandleTypeDescriptor;
import tech.intellispaces.jaquarius.system.Injection;

/**
 * The object handle broker.
 */
public interface ObjectHandleAgent {

  ObjectHandleTypeDescriptor type();

  Action methodAction(int ordinal);

  Action guideAction(int ordinal);

  Injection injection(int ordinal);

  <D, H> void addProjection(Class<D> targetDomain, H target);

  <D, H> H mapTo(Class<D> targetDomain);
}
