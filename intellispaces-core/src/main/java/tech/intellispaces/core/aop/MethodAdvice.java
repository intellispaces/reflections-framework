package tech.intellispaces.core.aop;

import tech.intellispaces.javastatements.method.MethodStatement;

public interface MethodAdvice extends Advice {

  MethodStatement joinMethod();
}
