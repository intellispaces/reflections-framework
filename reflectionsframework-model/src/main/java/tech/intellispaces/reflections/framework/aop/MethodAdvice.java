package tech.intellispaces.reflections.framework.aop;

import tech.intellispaces.jstatements.method.MethodStatement;

public interface MethodAdvice extends Advice {

  MethodStatement joinMethod();
}
