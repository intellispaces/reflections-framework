package intellispaces.framework.core.action;

import intellispaces.common.action.AbstractAction3;
import intellispaces.common.base.type.Type;
import intellispaces.framework.core.guide.GuideForm;
import intellispaces.framework.core.guide.n2.Mover2;
import intellispaces.framework.core.space.channel.Channel2;
import intellispaces.framework.core.system.Modules;

class MoveThruChannel2Action<S, Q1, Q2> extends AbstractAction3<S, S, Q1, Q2> {
  private final Type<S> sourceType;
  private final Class<? extends Channel2> channelClass;
  private final GuideForm guideForm;
  private final Mover2<S, Q1, Q2> autoMover;

  MoveThruChannel2Action(
      Type<S> sourceType,
      Class<? extends Channel2> channelClass,
      GuideForm guideForm
  ) {
    this.sourceType = sourceType;
    this.channelClass = channelClass;
    this.guideForm = guideForm;
    this.autoMover = Modules.current().autoMoverThruChannel2(sourceType, channelClass, guideForm);
  }

  @Override
  public S execute(S source, Q1 qualifier1, Q2 qualifier2) {
    return autoMover.move(source, qualifier1, qualifier2);
  }
}
