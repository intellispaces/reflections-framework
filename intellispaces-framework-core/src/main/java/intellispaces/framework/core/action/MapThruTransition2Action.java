package intellispaces.framework.core.action;

import intellispaces.common.action.AbstractAction3;
import intellispaces.common.javastatement.type.Type;
import intellispaces.framework.core.guide.n2.Mapper2;
import intellispaces.framework.core.space.transition.Transition2;
import intellispaces.framework.core.system.Modules;

class MapThruTransition2Action<T, S, Q1, Q2> extends AbstractAction3<T, S, Q1, Q2> {
  private final Type<S> sourceType;
  private final Class<? extends Transition2> transitionClass;
  private final Mapper2<S, T, Q1, Q2> autoMapper;

  MapThruTransition2Action(
      Type<S> sourceType,
      Class<? extends Transition2> transitionClass
  ) {
    this.sourceType = sourceType;
    this.transitionClass = transitionClass;
    this.autoMapper = Modules.current().autoMapperThruTransition2(sourceType, transitionClass);
  }

  @Override
  public T execute(S source, Q1 qualifier1, Q2 qualifier2) {
    return autoMapper.map(source, qualifier1, qualifier2);
  }
}
