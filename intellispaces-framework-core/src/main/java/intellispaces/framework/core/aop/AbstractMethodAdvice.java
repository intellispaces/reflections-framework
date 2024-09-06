package intellispaces.framework.core.aop;

import intellispaces.common.action.Action;
import intellispaces.common.action.wrapper.AbstractWrapper;
import intellispaces.common.javastatement.method.MethodStatement;

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
