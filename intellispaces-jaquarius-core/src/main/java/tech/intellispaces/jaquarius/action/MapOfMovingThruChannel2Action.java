package tech.intellispaces.jaquarius.action;

import tech.intellispaces.commons.action.AbstractAction3;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.jaquarius.channel.Channel2;
import tech.intellispaces.jaquarius.guide.n2.MapperOfMoving2;
import tech.intellispaces.jaquarius.object.reference.ObjectForm;
import tech.intellispaces.jaquarius.system.Modules;

class MapOfMovingThruChannel2Action<T, S, Q1, Q2> extends AbstractAction3<T, S, Q1, Q2> {
  private final Type<S> sourceType;
  private final Class<? extends Channel2> channelClass;
  private final ObjectForm targetForm;
  private final MapperOfMoving2<S, T, Q1, Q2> autoMapper;

  MapOfMovingThruChannel2Action(
      Type<S> sourceType,
      Class<? extends Channel2> channelClass,
      ObjectForm targetForm
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
