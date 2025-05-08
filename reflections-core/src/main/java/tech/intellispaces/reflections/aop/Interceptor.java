package tech.intellispaces.reflections.aop;

import tech.intellispaces.actions.Action;
import tech.intellispaces.reflections.system.ProjectionProvider;
import tech.intellispaces.jstatements.method.MethodStatement;

public abstract class Interceptor extends AbstractMethodAdvice {

  public Interceptor(MethodStatement joinPoint, Action nextAction, ProjectionProvider projectionProvider) {
    super(joinPoint, nextAction, projectionProvider);
  }
}
