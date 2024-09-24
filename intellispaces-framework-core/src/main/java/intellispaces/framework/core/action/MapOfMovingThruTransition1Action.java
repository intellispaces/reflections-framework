package intellispaces.framework.core.action;

import intellispaces.common.action.AbstractAction2;
import intellispaces.common.base.type.Type;
import intellispaces.framework.core.guide.n1.MapperOfMoving1;
import intellispaces.framework.core.space.transition.Transition1;
import intellispaces.framework.core.system.Modules;

class MapOfMovingThruTransition1Action<T, S, Q> extends AbstractAction2<T, S, Q> {
  private final Type<S> sourceType;
  private final Class<? extends Transition1> transitionClass;
  private final MapperOfMoving1<S, T, Q> autoMover;

  MapOfMovingThruTransition1Action(
      Type<S> sourceType,
      Class<? extends Transition1> transitionClass
  ) {
    this.sourceType = sourceType;
    this.transitionClass = transitionClass;
    this.autoMover = Modules.current().autoMapperOfMovingThruTransition1(sourceType, transitionClass);
  }

  @Override
  public T execute(S source, Q qualifier) {
    return autoMover.map(source, qualifier);
  }
}
