package intellispaces.jaquarius.action;

import intellispaces.common.action.AbstractAction2;
import intellispaces.common.base.type.Type;
import intellispaces.jaquarius.channel.Channel1;
import intellispaces.jaquarius.guide.GuideForm;
import intellispaces.jaquarius.guide.n1.MapperOfMoving1;
import intellispaces.jaquarius.system.Modules;

class MapOfMovingThruChannel1Action<T, S, Q> extends AbstractAction2<T, S, Q> {
  private final Type<S> sourceType;
  private final Class<? extends Channel1> channelClass;
  private final GuideForm guideForm;
  private final MapperOfMoving1<S, T, Q> autoMover;

  MapOfMovingThruChannel1Action(
      Type<S> sourceType,
      Class<? extends Channel1> channelClass,
      GuideForm guideForm
  ) {
    this.sourceType = sourceType;
    this.channelClass = channelClass;
    this.guideForm = guideForm;
    this.autoMover = Modules.current().autoMapperOfMovingThruChannel1(sourceType, channelClass, guideForm);
  }

  @Override
  public T execute(S source, Q qualifier) {
    return autoMover.map(source, qualifier);
  }
}
