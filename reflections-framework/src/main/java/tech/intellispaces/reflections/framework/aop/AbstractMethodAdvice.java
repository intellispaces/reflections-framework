package tech.intellispaces.reflections.framework.aop;

import tech.intellispaces.actions.AbstractAction;
import tech.intellispaces.actions.Action;
import tech.intellispaces.jstatements.method.MethodStatement;
import tech.intellispaces.reflections.framework.engine.Engine;

public abstract class AbstractMethodAdvice extends AbstractAction implements MethodAdvice {
  private final Action joinAction;
  protected final MethodStatement joinMethod;
  protected final Engine engine;

  /**
   * The constructor.<p/>
   *
   * Join method and join action represents advice join point.
   */
  public AbstractMethodAdvice(MethodStatement joinMethod, Action joinAction, Engine engine) {
    this.joinAction = joinAction;
    this.joinMethod = joinMethod;
    this.engine = engine;
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
