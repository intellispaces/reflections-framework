package intellispaces.jaquarius.action;

import intellispaces.jaquarius.channel.Channel0;
import intellispaces.jaquarius.guide.GuideForm;
import intellispaces.jaquarius.guide.n0.Mover0;
import intellispaces.jaquarius.system.Modules;
import tech.intellispaces.action.AbstractAction1;
import tech.intellispaces.entity.type.Type;

class MoveThruChannel0Action<S> extends AbstractAction1<S, S> {
  private final Type<S> sourceType;
  private final Class<? extends Channel0> channelClass;
  private final GuideForm guideForm;
  private final Mover0<S> autoMover;

  MoveThruChannel0Action(
      Type<S> sourceType,
      Class<? extends Channel0> channelClass,
      GuideForm guideForm
  ) {
    this.sourceType = sourceType;
    this.channelClass = channelClass;
    this.guideForm = guideForm;
    this.autoMover = Modules.current().autoMoverThruChannel0(sourceType, channelClass, guideForm);
  }

  @Override
  public S execute(S source) {
    return autoMover.move(source);
  }
}
