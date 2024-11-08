package intellispaces.jaquarius.aop;

import intellispaces.common.action.Action;
import intellispaces.common.action.wrapper.AbstractWrapperAction;
import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.jaquarius.system.ProjectionProvider;

public abstract class AbstractMethodAdvice extends AbstractWrapperAction implements MethodAdvice {
  protected final MethodStatement joinMethod;
  protected final ProjectionProvider projectionProvider;

  /**
   * The constructor.<p/>
   *
   * Join method and join action represents advice join point.
   */
  public AbstractMethodAdvice(MethodStatement joinMethod, Action joinAction, ProjectionProvider projectionProvider) {
    super(joinAction);
    this.joinMethod = joinMethod;
    this.projectionProvider = projectionProvider;
  }

  @Override
  public MethodStatement joinMethod() {
    return joinMethod;
  }

  @Override
  public Action joinAction() {
    return wrappedAction();
  }
}
