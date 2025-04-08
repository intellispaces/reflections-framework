package tech.intellispaces.jaquarius.aop;

import tech.intellispaces.reflection.method.MethodStatement;

public interface MethodAdvice extends Advice {

  MethodStatement joinMethod();
}
