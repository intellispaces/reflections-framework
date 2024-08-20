package tech.intellispaces.core.aop;

import tech.intellispaces.actions.Action;
import tech.intellispaces.javastatements.method.MethodStatement;

public abstract class Interceptor extends AbstractMethodAdvice {

  public Interceptor(MethodStatement joinPoint, Action nextAction) {
    super(joinPoint, nextAction);
  }
}
