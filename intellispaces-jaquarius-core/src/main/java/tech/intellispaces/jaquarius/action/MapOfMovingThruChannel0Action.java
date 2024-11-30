package tech.intellispaces.jaquarius.action;

import tech.intellispaces.action.AbstractAction1;
import tech.intellispaces.entity.type.Type;
import tech.intellispaces.jaquarius.channel.Channel0;
import tech.intellispaces.jaquarius.guide.n0.MapperOfMoving0;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForm;
import tech.intellispaces.jaquarius.system.Modules;

class MapOfMovingThruChannel0Action<T, S> extends AbstractAction1<T, S> {
  private final Type<S> sourceType;
  private final Class<? extends Channel0> channelClass;
  private final ObjectReferenceForm targetForm;
  private final MapperOfMoving0<S, T> autoMapper;

  MapOfMovingThruChannel0Action(
      Type<S> sourceType,
      Class<? extends Channel0> channelClass,
      ObjectReferenceForm targetForm
  ) {
    this.sourceType = sourceType;
    this.channelClass = channelClass;
    this.targetForm = targetForm;
    this.autoMapper = Modules.current().autoMapperOfMovingThruChannel0(sourceType, channelClass, targetForm);
  }

  @Override
  public T execute(S source) {
    return autoMapper.map(source);
  }
}
