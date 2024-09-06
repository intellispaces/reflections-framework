package intellispaces.framework.core.aop;

import intellispaces.common.action.Action;
import intellispaces.common.javastatement.method.MethodStatement;

public abstract class Interceptor extends AbstractMethodAdvice {

  public Interceptor(MethodStatement joinPoint, Action nextAction) {
    super(joinPoint, nextAction);
  }
}
