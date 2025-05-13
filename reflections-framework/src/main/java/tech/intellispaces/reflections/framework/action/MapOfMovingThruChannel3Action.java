package tech.intellispaces.reflections.framework.action;

import tech.intellispaces.actions.AbstractAction4;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.reflections.framework.channel.Channel3;
import tech.intellispaces.reflections.framework.guide.n3.MapperOfMoving3;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.system.Modules;

class MapOfMovingThruChannel3Action<T, S, Q1, Q2, Q3> extends AbstractAction4<T, S, Q1, Q2, Q3> {
  private final Type<S> sourceType;
  private final Class<? extends Channel3> channelClass;
  private final ReflectionForm targetForm;
  private final MapperOfMoving3<S, T, Q1, Q2, Q3> autoMapper;

  MapOfMovingThruChannel3Action(
      Type<S> sourceType,
      Class<? extends Channel3> channelClass,
      ReflectionForm targetForm
  ) {
    this.sourceType = sourceType;
    this.channelClass = channelClass;
    this.targetForm = targetForm;
    this.autoMapper = Modules.current().autoMapperOfMovingThruChannel3(sourceType, channelClass, targetForm);
  }

  @Override
  public T execute(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3) {
    return autoMapper.map(source, qualifier1, qualifier2, qualifier3);
  }
}
