package intellispaces.core.aop;

import intellispaces.actions.Action;
import intellispaces.javastatements.method.MethodStatement;

public abstract class Interceptor extends AbstractMethodAdvice {

  public Interceptor(MethodStatement joinPoint, Action nextAction) {
    super(joinPoint, nextAction);
  }
}
