package intellispaces.framework.core.action;

import intellispaces.common.action.AbstractAction2;
import intellispaces.common.base.type.Type;
import intellispaces.framework.core.guide.GuideForm;
import intellispaces.framework.core.guide.n1.Mapper1;
import intellispaces.framework.core.space.channel.Channel1;
import intellispaces.framework.core.system.Modules;

class MapThruChannel1Action<T, S, Q> extends AbstractAction2<T, S, Q> {
  private final Type<S> sourceType;
  private final Class<? extends Channel1> channelClass;
  private final GuideForm guideForm;
  private final Mapper1<S, T, Q> autoMover;

  MapThruChannel1Action(
      Type<S> sourceType,
      Class<? extends Channel1> channelClass,
      GuideForm guideForm
  ) {
    this.sourceType = sourceType;
    this.channelClass = channelClass;
    this.guideForm = guideForm;
    this.autoMover = Modules.current().autoMapperThruChannel1(sourceType, channelClass, guideForm);
  }

  @Override
  public T execute(S source, Q qualifier) {
    return autoMover.map(source, qualifier);
  }
}
