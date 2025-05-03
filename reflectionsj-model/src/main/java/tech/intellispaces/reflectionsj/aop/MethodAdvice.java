package tech.intellispaces.reflectionsj.aop;

import tech.intellispaces.statementsj.method.MethodStatement;

public interface MethodAdvice extends Advice {

  MethodStatement joinMethod();
}
