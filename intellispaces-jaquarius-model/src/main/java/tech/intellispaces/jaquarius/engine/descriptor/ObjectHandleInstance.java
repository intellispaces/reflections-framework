package tech.intellispaces.jaquarius.engine.descriptor;

import tech.intellispaces.action.Action;
import tech.intellispaces.jaquarius.system.Injection;

/**
 * Object handle instance descriptor.
 */
public interface ObjectHandleInstance {

  ObjectHandleType type();

  Action methodAction(int ordinal);

  Action guideAction(int ordinal);

  Injection injection(int ordinal);

  <D, H> void addProjection(Class<D> targetDomain, H target);

  <D, H> H mapTo(Class<D> targetDomain);
}
