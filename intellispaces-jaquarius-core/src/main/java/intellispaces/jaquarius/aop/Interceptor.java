package intellispaces.jaquarius.aop;

import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.jaquarius.system.ProjectionProvider;
import tech.intellispaces.action.Action;

public abstract class Interceptor extends AbstractMethodAdvice {

  public Interceptor(MethodStatement joinPoint, Action nextAction, ProjectionProvider projectionProvider) {
    super(joinPoint, nextAction, projectionProvider);
  }
}
