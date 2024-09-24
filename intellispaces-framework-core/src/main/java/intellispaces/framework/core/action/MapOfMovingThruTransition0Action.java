package intellispaces.framework.core.action;

import intellispaces.common.action.AbstractAction1;
import intellispaces.common.base.type.Type;
import intellispaces.framework.core.guide.n0.MapperOfMoving0;
import intellispaces.framework.core.space.transition.Transition0;
import intellispaces.framework.core.system.Modules;

class MapOfMovingThruTransition0Action<T, S> extends AbstractAction1<T, S> {
  private final Type<S> sourceType;
  private final Class<? extends Transition0> transitionClass;
  private final MapperOfMoving0<S, T> autoMapper;

  MapOfMovingThruTransition0Action(
      Type<S> sourceType,
      Class<? extends Transition0> transitionClass
  ) {
    this.sourceType = sourceType;
    this.transitionClass = transitionClass;
    this.autoMapper = Modules.current().autoMapperOfMovingThruTransition0(sourceType, transitionClass);
  }

  @Override
  public T execute(S source) {
    return autoMapper.map(source);
  }
}
