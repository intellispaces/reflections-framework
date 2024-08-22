package intellispaces.core.aop;

import intellispaces.javastatements.method.MethodStatement;

public interface MethodAdvice extends Advice {

  MethodStatement joinMethod();
}
