package intellispaces.framework.core.action;

import intellispaces.common.action.AbstractAction4;
import intellispaces.common.base.type.Type;
import intellispaces.framework.core.guide.n3.MapperOfMoving3;
import intellispaces.framework.core.space.transition.Transition3;
import intellispaces.framework.core.system.Modules;

class MapOfMovingThruTransition3Action<T, S, Q1, Q2, Q3> extends AbstractAction4<T, S, Q1, Q2, Q3> {
  private final Type<S> sourceType;
  private final Class<? extends Transition3> transitionClass;
  private final MapperOfMoving3<S, T, Q1, Q2, Q3> autoMapper;

  MapOfMovingThruTransition3Action(
      Type<S> sourceType,
      Class<? extends Transition3> transitionClass
  ) {
    this.sourceType = sourceType;
    this.transitionClass = transitionClass;
    this.autoMapper = Modules.current().autoMapperOfMovingThruTransition3(sourceType, transitionClass);
  }

  @Override
  public T execute(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3) {
    return autoMapper.map(source, qualifier1, qualifier2, qualifier3);
  }
}
