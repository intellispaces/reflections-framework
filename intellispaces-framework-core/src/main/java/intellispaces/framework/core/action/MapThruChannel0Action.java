package intellispaces.framework.core.action;

import intellispaces.common.action.AbstractAction1;
import intellispaces.common.base.type.Type;
import intellispaces.framework.core.guide.n0.Mapper0;
import intellispaces.framework.core.space.channel.Channel0;
import intellispaces.framework.core.system.Modules;

class MapThruChannel0Action<T, S> extends AbstractAction1<T, S> {
  private final Type<S> sourceType;
  private final Class<? extends Channel0> channelClass;
  private final Mapper0<S, T> autoMapper;

  MapThruChannel0Action(
      Type<S> sourceType,
      Class<? extends Channel0> channelClass
  ) {
    this.sourceType = sourceType;
    this.channelClass = channelClass;
    this.autoMapper = Modules.current().autoMapperThruChannel0(sourceType, channelClass);
  }

  @Override
  public T execute(S source) {
    return autoMapper.map(source);
  }
}
