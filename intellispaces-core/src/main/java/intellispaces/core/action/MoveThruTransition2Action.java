package intellispaces.core.action;

import intellispaces.actions.AbstractAction3;
import intellispaces.core.guide.n2.Mover2;
import intellispaces.core.space.transition.Transition2;
import intellispaces.core.system.Modules;
import intellispaces.javastatements.type.Type;

class MoveThruTransition2Action<B, S, Q1, Q2> extends AbstractAction3<B, S, Q1, Q2> {
  private final Type<S> sourceType;
  private final Class<? extends Transition2> transitionClass;
  private final Mover2<S, B, Q1, Q2> autoMover;

  MoveThruTransition2Action(
      Type<S> sourceType,
      Class<? extends Transition2> transitionClass
  ) {
    this.sourceType = sourceType;
    this.transitionClass = transitionClass;
    this.autoMover = Modules.current().autoMoverThruTransition2(sourceType, transitionClass);
  }

  @Override
  public B execute(S source, Q1 qualifier1, Q2 qualifier2) {
    return autoMover.move(source, qualifier1, qualifier2);
  }
}
