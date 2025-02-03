package tech.intellispaces.jaquarius.action;

import tech.intellispaces.commons.action.AbstractAction2;
import tech.intellispaces.commons.base.type.Type;
import tech.intellispaces.jaquarius.channel.Channel1;
import tech.intellispaces.jaquarius.guide.n1.MapperOfMoving1;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForm;
import tech.intellispaces.jaquarius.system.Modules;

class MapOfMovingThruChannel1Action<T, S, Q> extends AbstractAction2<T, S, Q> {
  private final Type<S> sourceType;
  private final Class<? extends Channel1> channelClass;
  private final ObjectReferenceForm targetForm;
  private final MapperOfMoving1<S, T, Q> autoMover;

  MapOfMovingThruChannel1Action(
      Type<S> sourceType,
      Class<? extends Channel1> channelClass,
      ObjectReferenceForm targetForm
  ) {
    this.sourceType = sourceType;
    this.channelClass = channelClass;
    this.targetForm = targetForm;
    this.autoMover = Modules.current().autoMapperOfMovingThruChannel1(sourceType, channelClass, targetForm);
  }

  @Override
  public T execute(S source, Q qualifier) {
    return autoMover.map(source, qualifier);
  }
}
