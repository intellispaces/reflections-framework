package tech.intellispaces.jaquarius.engine;

import tech.intellispaces.actions.Action;
import tech.intellispaces.jaquarius.engine.description.ObjectHandleTypeDescription;
import tech.intellispaces.jaquarius.object.reference.ObjectHandle;
import tech.intellispaces.jaquarius.system.Injection;

import java.util.List;

/**
 * The object handle broker.
 */
public interface ObjectHandleBroker {

  ObjectHandleTypeDescription type();

  Action methodAction(int ordinal);

  Action guideAction(int ordinal);

  Injection injection(int ordinal);

  <D, H> void addProjection(Class<D> targetDomain, H target);

  List<? extends ObjectHandle<?>> underlyingHandles();

  ObjectHandle<?> overlyingHandle();

  void setOverlyingHandle(ObjectHandle<?> overlyingHandle);

  <D, H> H mapTo(Class<D> targetDomain);
}
