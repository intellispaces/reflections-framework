package tech.intellispaces.jaquarius.aop;

import tech.intellispaces.commons.action.AbstractAction;
import tech.intellispaces.commons.action.Action;
import tech.intellispaces.commons.reflection.method.MethodStatement;
import tech.intellispaces.jaquarius.system.ProjectionProvider;

public abstract class AbstractMethodAdvice extends AbstractAction implements MethodAdvice {
  private final Action joinAction;
  protected final MethodStatement joinMethod;
  protected final ProjectionProvider projectionProvider;

  /**
   * The constructor.<p/>
   *
   * Join method and join action represents advice join point.
   */
  public AbstractMethodAdvice(MethodStatement joinMethod, Action joinAction, ProjectionProvider projectionProvider) {
    this.joinAction = joinAction;
    this.joinMethod = joinMethod;
    this.projectionProvider = projectionProvider;
  }

  @Override
  public MethodStatement joinMethod() {
    return joinMethod;
  }

  @Override
  public Action joinAction() {
    return joinAction;
  }

  @Override
  public Action wrappedAction() {
    return joinAction;
  }
}
