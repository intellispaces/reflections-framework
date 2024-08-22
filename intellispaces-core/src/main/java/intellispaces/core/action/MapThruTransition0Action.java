package intellispaces.core.action;

import intellispaces.actions.AbstractAction1;
import intellispaces.core.guide.n0.Mapper0;
import intellispaces.core.space.transition.Transition0;
import intellispaces.core.system.Modules;
import intellispaces.javastatements.type.Type;

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
