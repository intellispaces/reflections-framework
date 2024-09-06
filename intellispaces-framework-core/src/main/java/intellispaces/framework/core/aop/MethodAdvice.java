package intellispaces.framework.core.aop;

import intellispaces.common.javastatement.method.MethodStatement;

public interface MethodAdvice extends Advice {

  MethodStatement joinMethod();
}
