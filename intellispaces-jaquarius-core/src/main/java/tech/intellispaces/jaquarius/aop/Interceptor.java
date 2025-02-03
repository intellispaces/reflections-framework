package tech.intellispaces.jaquarius.aop;

import tech.intellispaces.commons.action.Action;
import tech.intellispaces.commons.java.reflection.method.MethodStatement;
import tech.intellispaces.jaquarius.system.ProjectionProvider;

public abstract class Interceptor extends AbstractMethodAdvice {

  public Interceptor(MethodStatement joinPoint, Action nextAction, ProjectionProvider projectionProvider) {
    super(joinPoint, nextAction, projectionProvider);
  }
}
