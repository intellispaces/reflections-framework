package tech.intellispaces.jaquarius.action;

import tech.intellispaces.jaquarius.channel.Channel1;
import tech.intellispaces.jaquarius.guide.GuideForm;
import tech.intellispaces.jaquarius.guide.n1.Mapper1;
import tech.intellispaces.jaquarius.system.Modules;
import tech.intellispaces.action.AbstractAction2;
import tech.intellispaces.entity.type.Type;

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
