package tech.intellispaces.jaquarius.engine;

import tech.intellispaces.action.Action;
import tech.intellispaces.jaquarius.engine.description.ObjectHandleTypeDescription;
import tech.intellispaces.jaquarius.system.Injection;

/**
 * The object handle broker.
 */
public interface ObjectHandleBroker {

  ObjectHandleTypeDescription type();

  Action methodAction(int ordinal);

  Action guideAction(int ordinal);

  Injection injection(int ordinal);

  <D, H> void addProjection(Class<D> targetDomain, H target);

  <D, H> H mapTo(Class<D> targetDomain);
}
