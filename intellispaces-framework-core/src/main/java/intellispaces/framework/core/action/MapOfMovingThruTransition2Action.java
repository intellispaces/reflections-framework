package intellispaces.framework.core.action;

import intellispaces.common.action.AbstractAction3;
import intellispaces.common.base.type.Type;
import intellispaces.framework.core.guide.n2.MapperOfMoving2;
import intellispaces.framework.core.space.transition.Transition2;
import intellispaces.framework.core.system.Modules;

class MapOfMovingThruTransition2Action<T, S, Q1, Q2> extends AbstractAction3<T, S, Q1, Q2> {
  private final Type<S> sourceType;
  private final Class<? extends Transition2> transitionClass;
  private final MapperOfMoving2<S, T, Q1, Q2> autoMapper;

  MapOfMovingThruTransition2Action(
      Type<S> sourceType,
      Class<? extends Transition2> transitionClass
  ) {
    this.sourceType = sourceType;
    this.transitionClass = transitionClass;
    this.autoMapper = Modules.current().autoMapperOfMovingThruTransition2(sourceType, transitionClass);
  }

  @Override
  public T execute(S source, Q1 qualifier1, Q2 qualifier2) {
    return autoMapper.map(source, qualifier1, qualifier2);
  }
}
