package tech.intellispaces.core.action;

import tech.intellispaces.actions.AbstractAction2;
import tech.intellispaces.core.guide.n1.Mover1;
import tech.intellispaces.core.space.transition.Transition1;
import tech.intellispaces.core.system.Modules;
import tech.intellispaces.javastatements.type.Type;

class MoveThruTransition1Action<B, S, Q> extends AbstractAction2<B, S, Q> {
  private final Type<S> sourceType;
  private final Class<? extends Transition1> transitionClass;
  private final Mover1<S, B, Q> autoMover;

  MoveThruTransition1Action(
      Type<S> sourceType,
      Class<? extends Transition1> transitionClass
  ) {
    this.sourceType = sourceType;
    this.transitionClass = transitionClass;
    this.autoMover = Modules.current().autoMoverThruTransition1(sourceType, transitionClass);
  }

  @Override
  public B execute(S source, Q qualifier) {
    return autoMover.move(source, qualifier);
  }
}
