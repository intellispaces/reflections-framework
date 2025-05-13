package tech.intellispaces.reflections.framework.action;

import tech.intellispaces.actions.AbstractAction1;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.reflections.framework.channel.Channel0;
import tech.intellispaces.reflections.framework.guide.n0.MapperOfMoving0;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.system.Modules;

class MapOfMovingThruChannel0Action<T, S> extends AbstractAction1<T, S> {
  private final Type<S> sourceType;
  private final Class<? extends Channel0> channelClass;
  private final ReflectionForm targetForm;
  private final MapperOfMoving0<S, T> autoMapper;

  MapOfMovingThruChannel0Action(
      Type<S> sourceType,
      Class<? extends Channel0> channelClass,
      ReflectionForm targetForm
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
