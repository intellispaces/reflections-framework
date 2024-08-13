package tech.intellispaces.core.aop;

import tech.intellispaces.actions.Action;
import tech.intellispaces.javastatements.method.MethodStatement;

public abstract class InterceptorAdvice extends AbstractMethodAdvice {

  public InterceptorAdvice(MethodStatement joinPoint, Action nextAction) {
    super(joinPoint, nextAction);
  }
}
