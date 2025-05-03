package tech.intellispaces.reflections.action;

import tech.intellispaces.actions.AbstractAction3;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.reflections.channel.Channel2;
import tech.intellispaces.reflections.guide.n2.Mover2;
import tech.intellispaces.reflections.object.reference.ObjectReferenceForm;
import tech.intellispaces.reflections.system.Modules;

class MoveThruChannel2Action<S, Q1, Q2> extends AbstractAction3<S, S, Q1, Q2> {
  private final Type<S> sourceType;
  private final Class<? extends Channel2> channelClass;
  private final ObjectReferenceForm targetForm;
  private final Mover2<S, Q1, Q2> autoMover;

  MoveThruChannel2Action(
      Type<S> sourceType,
      Class<? extends Channel2> channelClass,
      ObjectReferenceForm targetForm
  ) {
    this.sourceType = sourceType;
    this.channelClass = channelClass;
    this.targetForm = targetForm;
    this.autoMover = Modules.current().autoMoverThruChannel2(sourceType, channelClass, targetForm);
  }

  @Override
  public S execute(S source, Q1 qualifier1, Q2 qualifier2) {
    return autoMover.move(source, qualifier1, qualifier2);
  }
}
