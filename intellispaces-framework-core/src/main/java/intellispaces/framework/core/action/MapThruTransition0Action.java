package intellispaces.framework.core.action;

import intellispaces.common.action.AbstractAction1;
import intellispaces.common.base.type.Type;
import intellispaces.framework.core.guide.n0.Mapper0;
import intellispaces.framework.core.space.transition.Transition0;
import intellispaces.framework.core.system.Modules;

class MapThruTransition0Action<T, S> extends AbstractAction1<T, S> {
  private final Type<S> sourceType;
  private final Class<? extends Transition0> transitionClass;
  private final Mapper0<S, T> autoMapper;

  MapThruTransition0Action(
      Type<S> sourceType,
      Class<? extends Transition0> transitionClass
  ) {
    this.sourceType = sourceType;
    this.transitionClass = transitionClass;
    this.autoMapper = Modules.current().autoMapperThruTransition0(sourceType, transitionClass);
  }

  @Override
  public T execute(S source) {
    return autoMapper.map(source);
  }
}
