package intellispaces.jaquarius.action;

import intellispaces.common.action.AbstractAction5;
import intellispaces.common.base.type.Type;
import intellispaces.jaquarius.channel.Channel4;
import intellispaces.jaquarius.guide.GuideForm;
import intellispaces.jaquarius.guide.n4.MapperOfMoving4;
import intellispaces.jaquarius.system.Modules;

class MapOfMovingThruChannel4Action<T, S, Q1, Q2, Q3, Q4> extends AbstractAction5<T, S, Q1, Q2, Q3, Q4> {
  private final Type<S> sourceType;
  private final Class<? extends Channel4> channelClass;
  private final GuideForm guideForm;
  private final MapperOfMoving4<S, T, Q1, Q2, Q3, Q4> autoMapper;

  MapOfMovingThruChannel4Action(
      Type<S> sourceType,
      Class<? extends Channel4> channelClass,
      GuideForm guideForm
  ) {
    this.sourceType = sourceType;
    this.channelClass = channelClass;
    this.guideForm = guideForm;
    this.autoMapper = Modules.current().autoMapperOfMovingThruChannel4(sourceType, channelClass, guideForm);
  }

  @Override
  public T execute(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Q4 qualifier4) {
    return autoMapper.map(source, qualifier1, qualifier2, qualifier3, qualifier4);
  }
}
