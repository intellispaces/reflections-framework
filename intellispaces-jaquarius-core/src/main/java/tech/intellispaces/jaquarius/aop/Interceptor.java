package tech.intellispaces.jaquarius.aop;

import tech.intellispaces.action.Action;
import tech.intellispaces.jaquarius.system.ProjectionProvider;
import tech.intellispaces.java.reflection.method.MethodStatement;

public abstract class Interceptor extends AbstractMethodAdvice {

  public Interceptor(MethodStatement joinPoint, Action nextAction, ProjectionProvider projectionProvider) {
    super(joinPoint, nextAction, projectionProvider);
  }
}
