package tech.intellispaces.reflections.framework.engine;

import java.util.List;

import tech.intellispaces.actions.Action;
import tech.intellispaces.reflections.framework.engine.description.ObjectHandleTypeDescription;
import tech.intellispaces.reflections.framework.reflection.Reflection;
import tech.intellispaces.reflections.framework.system.Injection;

/**
 * The object handle broker.
 */
public interface ObjectHandleBroker {

  ObjectHandleTypeDescription type();

  Action methodAction(int ordinal);

  Action guideAction(int ordinal);

  Injection injection(int ordinal);

  <D, R> void addProjection(Class<D> targetDomain, R target);

  List<? extends Reflection<?>> underlyingHandles();

  Reflection<?> overlyingHandle();

  void setOverlyingHandle(Reflection<?> overlyingHandle);

  <D, R> R mapTo(Class<D> targetDomain);
}
