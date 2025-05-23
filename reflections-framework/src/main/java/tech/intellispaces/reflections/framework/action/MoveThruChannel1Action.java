package tech.intellispaces.reflections.framework.action;

import tech.intellispaces.actions.AbstractAction2;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.reflections.framework.channel.Channel1;
import tech.intellispaces.reflections.framework.guide.n1.Mover1;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.system.Modules;

class MoveThruChannel1Action<S, Q> extends AbstractAction2<S, S, Q> {
  private final Type<S> sourceType;
  private final Class<? extends Channel1> channelClass;
  private final ReflectionForm targetForm;
  private final Mover1<S, Q> autoMover;

  MoveThruChannel1Action(
      Type<S> sourceType,
      Class<? extends Channel1> channelClass,
      ReflectionForm targetForm
  ) {
    this.sourceType = sourceType;
    this.channelClass = channelClass;
    this.targetForm = targetForm;
    this.autoMover = Modules.current().autoMoverThruChannel1(sourceType, channelClass, targetForm);
  }

  @Override
  public S execute(S source, Q qualifier) {
    return autoMover.move(source, qualifier);
  }
}
