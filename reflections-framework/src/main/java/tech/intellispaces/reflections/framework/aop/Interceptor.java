package tech.intellispaces.reflections.framework.aop;

import tech.intellispaces.actions.Action;
import tech.intellispaces.jstatements.method.MethodStatement;
import tech.intellispaces.reflections.framework.engine.ProjectionRegistry;

public abstract class Interceptor extends AbstractMethodAdvice {

  public Interceptor(MethodStatement joinPoint, Action nextAction, ProjectionRegistry projectionRegistry) {
    super(joinPoint, nextAction, projectionRegistry);
  }
}
