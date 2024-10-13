package intellispaces.framework.core.action;

import intellispaces.common.action.AbstractAction4;
import intellispaces.common.base.type.Type;
import intellispaces.framework.core.guide.GuideForm;
import intellispaces.framework.core.guide.n3.Mapper3;
import intellispaces.framework.core.space.channel.Channel3;
import intellispaces.framework.core.system.Modules;

class MapThruChannel3Action<T, S, Q1, Q2, Q3> extends AbstractAction4<T, S, Q1, Q2, Q3> {
  private final Type<S> sourceType;
  private final Class<? extends Channel3> channelClass;
  private final GuideForm guideForm;
  private final Mapper3<S, T, Q1, Q2, Q3> autoMapper;

  MapThruChannel3Action(
      Type<S> sourceType,
      Class<? extends Channel3> channelClass,
      GuideForm guideForm
  ) {
    this.sourceType = sourceType;
    this.channelClass = channelClass;
    this.guideForm = guideForm;
    this.autoMapper = Modules.current().autoMapperThruChannel3(sourceType, channelClass, guideForm);
  }

  @Override
  public T execute(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3) {
    return autoMapper.map(source, qualifier1, qualifier2, qualifier3);
  }
}
