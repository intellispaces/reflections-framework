package tech.intellispaces.reflectionsframework.aop;

import tech.intellispaces.jstatements.method.MethodStatement;

public interface MethodAdvice extends Advice {

  MethodStatement joinMethod();
}
