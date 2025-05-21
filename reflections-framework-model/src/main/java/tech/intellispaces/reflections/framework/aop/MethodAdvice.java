package tech.intellispaces.reflections.framework.aop;

import tech.intellispaces.javareflection.method.MethodStatement;

public interface MethodAdvice extends Advice {

  MethodStatement joinMethod();
}
