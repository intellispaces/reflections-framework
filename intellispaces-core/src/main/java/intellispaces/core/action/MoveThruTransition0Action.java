package intellispaces.core.action;

import intellispaces.actions.AbstractAction1;
import intellispaces.core.guide.n0.Mover0;
import intellispaces.core.space.transition.Transition0;
import intellispaces.core.system.Modules;
import intellispaces.javastatements.type.Type;

class MoveThruTransition0Action<B, S> extends AbstractAction1<B, S> {
  private final Type<S> sourceType;
  private final Class<? extends Transition0> transitionClass;
  private final Mover0<S, B> autoMover;

  MoveThruTransition0Action(
      Type<S> sourceType,
      Class<? extends Transition0> transitionClass
  ) {
    this.sourceType = sourceType;
    this.transitionClass = transitionClass;
    this.autoMover = Modules.current().autoMoverThruTransition0(sourceType, transitionClass);
  }

  @Override
  public B execute(S source) {
    return autoMover.move(source);
  }
}
