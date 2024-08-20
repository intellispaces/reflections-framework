package tech.intellispaces.core.aop;

import tech.intellispaces.actions.Action;
import tech.intellispaces.actions.wrapper.AbstractWrapper;
import tech.intellispaces.javastatements.method.MethodStatement;

public abstract class AbstractMethodAdvice extends AbstractWrapper implements MethodAdvice {
  protected final MethodStatement joinMethod;

  /**
   * The constructor.<p/>
   *
   * Join method and join action represents advice join point.
   */
  public AbstractMethodAdvice(MethodStatement joinMethod, Action joinAction) {
    super(joinAction);
    this.joinMethod = joinMethod;
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
