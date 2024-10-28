package intellispaces.jaquarius.aop;

import intellispaces.common.javastatement.method.MethodStatement;

public interface MethodAdvice extends Advice {

  MethodStatement joinMethod();
}
