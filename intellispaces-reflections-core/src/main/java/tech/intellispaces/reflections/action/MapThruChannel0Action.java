package tech.intellispaces.reflections.action;

import tech.intellispaces.actions.AbstractAction1;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.reflections.channel.Channel0;
import tech.intellispaces.reflections.guide.n0.Mapper0;
import tech.intellispaces.reflections.object.reference.ObjectReferenceForm;
import tech.intellispaces.reflections.system.Modules;

class MapThruChannel0Action<T, S> extends AbstractAction1<T, S> {
  private final Type<S> sourceType;
  private final Class<? extends Channel0> channelClass;
  private final ObjectReferenceForm targetForm;
  private final Mapper0<S, T> autoMapper;

  MapThruChannel0Action(
      Type<S> sourceType,
      Class<? extends Channel0> channelClass,
      ObjectReferenceForm targetForm
  ) {
    this.sourceType = sourceType;
    this.channelClass = channelClass;
    this.targetForm = targetForm;
    this.autoMapper = Modules.current().autoMapperThruChannel0(sourceType, channelClass, targetForm);
  }

  @Override
  public T execute(S source) {
    return autoMapper.map(source);
  }

  @Override
  public int executeReturnInt(S source) {
    return autoMapper.mapToInt(source);
  }

  @Override
  public double executeReturnDouble(S source) {
    return autoMapper.mapToDouble(source);
  }
}
