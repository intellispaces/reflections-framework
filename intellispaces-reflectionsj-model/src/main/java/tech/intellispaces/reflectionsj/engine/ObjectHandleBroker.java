package tech.intellispaces.reflectionsj.engine;

import java.util.List;

import tech.intellispaces.actions.Action;
import tech.intellispaces.reflectionsj.engine.description.ObjectHandleTypeDescription;
import tech.intellispaces.reflectionsj.object.reference.ObjectHandle;
import tech.intellispaces.reflectionsj.system.Injection;

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
