package intellispaces.framework.core.action;

import intellispaces.common.action.AbstractAction3;
import intellispaces.common.base.type.Type;
import intellispaces.framework.core.guide.GuideForm;
import intellispaces.framework.core.guide.n2.Mapper2;
import intellispaces.framework.core.space.channel.Channel2;
import intellispaces.framework.core.system.Modules;

class MapThruChannel2Action<T, S, Q1, Q2> extends AbstractAction3<T, S, Q1, Q2> {
  private final Type<S> sourceType;
  private final Class<? extends Channel2> channelClass;
  private final GuideForm guideForm;
  private final Mapper2<S, T, Q1, Q2> autoMapper;

  MapThruChannel2Action(
      Type<S> sourceType,
      Class<? extends Channel2> channelClass,
      GuideForm guideForm
  ) {
    this.sourceType = sourceType;
    this.channelClass = channelClass;
    this.guideForm = guideForm;
    this.autoMapper = Modules.current().autoMapperThruChannel2(sourceType, channelClass, guideForm);
  }

  @Override
  public T execute(S source, Q1 qualifier1, Q2 qualifier2) {
    return autoMapper.map(source, qualifier1, qualifier2);
  }
}
