package tech.intellispaces.jaquarius.action;

import tech.intellispaces.jaquarius.channel.Channel2;
import tech.intellispaces.jaquarius.guide.GuideForm;
import tech.intellispaces.jaquarius.guide.n2.Mapper2;
import tech.intellispaces.jaquarius.system.Modules;
import tech.intellispaces.action.AbstractAction3;
import tech.intellispaces.entity.type.Type;

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
