package intellispaces.framework.core.action;

import intellispaces.common.action.AbstractAction3;
import intellispaces.common.javastatement.type.Type;
import intellispaces.framework.core.guide.n2.Mover2;
import intellispaces.framework.core.space.transition.Transition2;
import intellispaces.framework.core.system.Modules;

class MoveThruTransition2Action<R, S, Q1, Q2> extends AbstractAction3<R, S, Q1, Q2> {
  private final Type<S> sourceType;
  private final Class<? extends Transition2> transitionClass;
  private final Mover2<S, R, Q1, Q2> autoMover;

  MoveThruTransition2Action(
      Type<S> sourceType,
      Class<? extends Transition2> transitionClass
  ) {
    this.sourceType = sourceType;
    this.transitionClass = transitionClass;
    this.autoMover = Modules.current().autoMoverThruTransition2(sourceType, transitionClass);
  }

  @Override
  public R execute(S source, Q1 qualifier1, Q2 qualifier2) {
    return autoMover.move(source, qualifier1, qualifier2);
  }
}
