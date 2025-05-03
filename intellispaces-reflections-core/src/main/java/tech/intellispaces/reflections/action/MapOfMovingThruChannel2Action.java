package tech.intellispaces.reflections.action;

import tech.intellispaces.actions.AbstractAction3;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.reflections.channel.Channel2;
import tech.intellispaces.reflections.guide.n2.MapperOfMoving2;
import tech.intellispaces.reflections.object.reference.ObjectReferenceForm;
import tech.intellispaces.reflections.system.Modules;

class MapOfMovingThruChannel2Action<T, S, Q1, Q2> extends AbstractAction3<T, S, Q1, Q2> {
  private final Type<S> sourceType;
  private final Class<? extends Channel2> channelClass;
  private final ObjectReferenceForm targetForm;
  private final MapperOfMoving2<S, T, Q1, Q2> autoMapper;

  MapOfMovingThruChannel2Action(
      Type<S> sourceType,
      Class<? extends Channel2> channelClass,
      ObjectReferenceForm targetForm
  ) {
    this.sourceType = sourceType;
    this.channelClass = channelClass;
    this.targetForm = targetForm;
    this.autoMapper = Modules.current().autoMapperOfMovingThruChannel2(sourceType, channelClass, targetForm);
  }

  @Override
  public T execute(S source, Q1 qualifier1, Q2 qualifier2) {
    return autoMapper.map(source, qualifier1, qualifier2);
  }
}
