package intellispaces.framework.core.action;

import intellispaces.common.action.AbstractAction2;
import intellispaces.common.base.type.Type;
import intellispaces.framework.core.guide.n1.MapperOfMoving1;
import intellispaces.framework.core.space.channel.Channel1;
import intellispaces.framework.core.system.Modules;

class MapOfMovingThruChannel1Action<T, S, Q> extends AbstractAction2<T, S, Q> {
  private final Type<S> sourceType;
  private final Class<? extends Channel1> channelClass;
  private final MapperOfMoving1<S, T, Q> autoMover;

  MapOfMovingThruChannel1Action(
      Type<S> sourceType,
      Class<? extends Channel1> channelClass
  ) {
    this.sourceType = sourceType;
    this.channelClass = channelClass;
    this.autoMover = Modules.current().autoMapperOfMovingThruChannel1(sourceType, channelClass);
  }

  @Override
  public T execute(S source, Q qualifier) {
    return autoMover.map(source, qualifier);
  }
}
