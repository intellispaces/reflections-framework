package tech.intellispaces.jaquarius.action;

import tech.intellispaces.action.AbstractAction5;
import tech.intellispaces.entity.type.Type;
import tech.intellispaces.jaquarius.channel.Channel4;
import tech.intellispaces.jaquarius.guide.n4.MapperOfMoving4;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForm;
import tech.intellispaces.jaquarius.system.Modules;

class MapOfMovingThruChannel4Action<T, S, Q1, Q2, Q3, Q4> extends AbstractAction5<T, S, Q1, Q2, Q3, Q4> {
  private final Type<S> sourceType;
  private final Class<? extends Channel4> channelClass;
  private final ObjectReferenceForm targetForm;
  private final MapperOfMoving4<S, T, Q1, Q2, Q3, Q4> autoMapper;

  MapOfMovingThruChannel4Action(
      Type<S> sourceType,
      Class<? extends Channel4> channelClass,
      ObjectReferenceForm targetForm
  ) {
    this.sourceType = sourceType;
    this.channelClass = channelClass;
    this.targetForm = targetForm;
    this.autoMapper = Modules.current().autoMapperOfMovingThruChannel4(sourceType, channelClass, targetForm);
  }

  @Override
  public T execute(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Q4 qualifier4) {
    return autoMapper.map(source, qualifier1, qualifier2, qualifier3, qualifier4);
  }
}
