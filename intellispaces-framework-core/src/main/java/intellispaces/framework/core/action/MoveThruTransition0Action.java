package intellispaces.framework.core.action;

import intellispaces.common.action.AbstractAction1;
import intellispaces.common.base.type.Type;
import intellispaces.framework.core.guide.n0.Mover0;
import intellispaces.framework.core.space.transition.Transition0;
import intellispaces.framework.core.system.Modules;

class MoveThruTransition0Action<S> extends AbstractAction1<S, S> {
  private final Type<S> sourceType;
  private final Class<? extends Transition0> transitionClass;
  private final Mover0<S> autoMover;

  MoveThruTransition0Action(
      Type<S> sourceType,
      Class<? extends Transition0> transitionClass
  ) {
    this.sourceType = sourceType;
    this.transitionClass = transitionClass;
    this.autoMover = Modules.current().autoMoverThruTransition0(sourceType, transitionClass);
  }

  @Override
  public S execute(S source) {
    return autoMover.move(source);
  }
}
