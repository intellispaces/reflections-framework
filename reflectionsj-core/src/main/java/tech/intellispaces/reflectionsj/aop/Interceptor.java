package tech.intellispaces.reflectionsj.aop;

import tech.intellispaces.actions.Action;
import tech.intellispaces.reflectionsj.system.ProjectionProvider;
import tech.intellispaces.statementsj.method.MethodStatement;

public abstract class Interceptor extends AbstractMethodAdvice {

  public Interceptor(MethodStatement joinPoint, Action nextAction, ProjectionProvider projectionProvider) {
    super(joinPoint, nextAction, projectionProvider);
  }
}
