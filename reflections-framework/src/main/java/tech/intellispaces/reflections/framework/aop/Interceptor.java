package tech.intellispaces.reflections.framework.aop;

import tech.intellispaces.actions.Action;
import tech.intellispaces.javareflection.method.MethodStatement;
import tech.intellispaces.reflections.framework.engine.Engine;

public abstract class Interceptor extends AbstractMethodAdvice {

  public Interceptor(MethodStatement joinPoint, Action nextAction, Engine engine) {
    super(joinPoint, nextAction, engine);
  }
}
