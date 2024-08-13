package tech.intellispaces.core.aop;

import tech.intellispaces.actions.Action;
import tech.intellispaces.actions.interceptor.AbstractInterceptor;
import tech.intellispaces.javastatements.method.MethodStatement;

public abstract class AbstractMethodAdvice extends AbstractInterceptor implements Advice {
  protected final MethodStatement joinPoint;

  public AbstractMethodAdvice(MethodStatement joinPoint, Action nextAction) {
    super(nextAction);
    this.joinPoint = joinPoint;
  }

  public MethodStatement joinPoint() {
    return joinPoint;
  }

  public Action nextAction() {
    return interceptedAction();
  }
}
