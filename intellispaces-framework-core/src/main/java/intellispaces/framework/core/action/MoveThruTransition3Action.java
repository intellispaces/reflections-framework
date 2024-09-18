package intellispaces.framework.core.action;

import intellispaces.common.action.AbstractAction4;
import intellispaces.common.javastatement.type.Type;
import intellispaces.framework.core.guide.n3.Mover3;
import intellispaces.framework.core.space.transition.Transition3;
import intellispaces.framework.core.system.Modules;

class MoveThruTransition3Action<R, S, Q1, Q2, Q3> extends AbstractAction4<R, S, Q1, Q2, Q3> {
  private final Type<S> sourceType;
  private final Class<? extends Transition3> transitionClass;
  private final Mover3<S, R, Q1, Q2, Q3> autoMover;

  MoveThruTransition3Action(
      Type<S> sourceType,
      Class<? extends Transition3> transitionClass
  ) {
    this.sourceType = sourceType;
    this.transitionClass = transitionClass;
    this.autoMover = Modules.current().autoMoverThruTransition3(sourceType, transitionClass);
  }

  @Override
  public R execute(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3) {
    return autoMover.move(source, qualifier1, qualifier2, qualifier3);
  }
}
