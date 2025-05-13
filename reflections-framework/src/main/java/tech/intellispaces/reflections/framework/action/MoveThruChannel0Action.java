package tech.intellispaces.reflections.framework.action;

import tech.intellispaces.actions.AbstractAction1;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.reflections.framework.channel.Channel0;
import tech.intellispaces.reflections.framework.guide.n0.Mover0;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.system.Modules;

class MoveThruChannel0Action<S> extends AbstractAction1<S, S> {
  private final Type<S> sourceType;
  private final Class<? extends Channel0> channelClass;
  private final ReflectionForm targetForm;
  private final Mover0<S> autoMover;

  MoveThruChannel0Action(
      Type<S> sourceType,
      Class<? extends Channel0> channelClass,
      ReflectionForm targetForm
  ) {
    this.sourceType = sourceType;
    this.channelClass = channelClass;
    this.targetForm = targetForm;
    this.autoMover = Modules.current().autoMoverThruChannel0(sourceType, channelClass, targetForm);
  }

  @Override
  public S execute(S source) {
    return autoMover.move(source);
  }
}
