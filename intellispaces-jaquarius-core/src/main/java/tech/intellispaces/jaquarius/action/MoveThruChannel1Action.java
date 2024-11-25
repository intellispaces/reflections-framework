package tech.intellispaces.jaquarius.action;

import tech.intellispaces.jaquarius.channel.Channel1;
import tech.intellispaces.jaquarius.guide.GuideForm;
import tech.intellispaces.jaquarius.guide.n1.Mover1;
import tech.intellispaces.jaquarius.system.Modules;
import tech.intellispaces.action.AbstractAction2;
import tech.intellispaces.entity.type.Type;

class MoveThruChannel1Action<S, Q> extends AbstractAction2<S, S, Q> {
  private final Type<S> sourceType;
  private final Class<? extends Channel1> channelClass;
  private final GuideForm guideForm;
  private final Mover1<S, Q> autoMover;

  MoveThruChannel1Action(
      Type<S> sourceType,
      Class<? extends Channel1> channelClass,
      GuideForm guideForm
  ) {
    this.sourceType = sourceType;
    this.channelClass = channelClass;
    this.guideForm = guideForm;
    this.autoMover = Modules.current().autoMoverThruChannel1(sourceType, channelClass, guideForm);
  }

  @Override
  public S execute(S source, Q qualifier) {
    return autoMover.move(source, qualifier);
  }
}
