package tech.intellispaces.jaquarius.aop;

import tech.intellispaces.commons.java.reflection.method.MethodStatement;

public interface MethodAdvice extends Advice {

  MethodStatement joinMethod();
}
