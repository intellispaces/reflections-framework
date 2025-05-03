package tech.intellispaces.reflectionsj.action;

import tech.intellispaces.actions.AbstractAction2;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.reflectionsj.channel.Channel1;
import tech.intellispaces.reflectionsj.guide.n1.MapperOfMoving1;
import tech.intellispaces.reflectionsj.object.reference.ObjectReferenceForm;
import tech.intellispaces.reflectionsj.system.Modules;

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
