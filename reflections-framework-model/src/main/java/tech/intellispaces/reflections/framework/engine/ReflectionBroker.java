package tech.intellispaces.reflections.framework.engine;

import java.util.List;

import tech.intellispaces.actions.Action;
import tech.intellispaces.reflections.framework.engine.description.ReflectionImplementationType;
import tech.intellispaces.reflections.framework.reflection.Reflection;
import tech.intellispaces.reflections.framework.system.Injection;

/**
 * The reflection broker.
 */
public interface ReflectionBroker {

  ReflectionImplementationType type();

  Action methodAction(int ordinal);

  Action guideAction(int ordinal);

  Injection injection(int ordinal);

  <D, R> void addProjection(Class<D> targetDomain, R target);

  List<? extends Reflection<?>> underlyingReflections();

  Reflection<?> overlyingReflection();

  void setOverlyingReflection(Reflection<?> overlyingReflection);

  <D, R> R mapTo(Class<D> targetDomain);
}
