package intellispaces.framework.core.action;

import intellispaces.common.action.Action1;
import intellispaces.common.action.Action2;
import intellispaces.common.action.Action3;
import intellispaces.common.action.Action4;
import intellispaces.common.base.type.Type;
import intellispaces.framework.core.space.channel.Channel0;
import intellispaces.framework.core.space.channel.Channel1;
import intellispaces.framework.core.space.channel.Channel2;
import intellispaces.framework.core.space.channel.Channel3;

public interface TraverseActions {

  static <S, T> Action1<T, S> mapThruChannel0(
    Type<S> sourceType, Class<? extends Channel0> channelClass
  ) {
    return new MapThruChannel0Action<>(sourceType, channelClass);
  }

  static <S, T, Q> Action2<T, S, Q> mapThruChannel1(
    Type<S> sourceType, Class<? extends Channel1> channelClass
  ) {
    return new MapThruChannel1Action<>(sourceType, channelClass);
  }

  static <S, T, Q1, Q2> Action3<T, S, Q1, Q2> mapThruChannel2(
    Type<S> sourceType, Class<? extends Channel2> channelClass
  ) {
    return new MapThruChannel2Action<>(sourceType, channelClass);
  }

  static <S, T, Q1, Q2, Q3> Action4<T, S, Q1, Q2, Q3> mapThruChannel3(
    Type<S> sourceType, Class<? extends Channel3> channelClass
  ) {
    return new MapThruChannel3Action<>(sourceType, channelClass);
  }

  static <S> Action1<S, S> moveThruChannel0(
    Type<S> sourceType, Class<? extends Channel0> channelClass
  ) {
    return new MoveThruChannel0Action<>(sourceType, channelClass);
  }

  static <S, Q> Action2<S, S, Q> moveThruChannel1(
    Type<S> sourceType, Class<? extends Channel1> channelClass
  ) {
    return new MoveThruChannel1Action<>(sourceType, channelClass);
  }

  static <S, Q1, Q2> Action3<S, S, Q1, Q2> moveThruChannel2(
    Type<S> sourceType, Class<? extends Channel2> channelClass
  ) {
    return new MoveThruChannel2Action<>(sourceType, channelClass);
  }

  static <S, Q1, Q2, Q3> Action4<S, S, Q1, Q2, Q3> moveThruChannel3(
    Type<S> sourceType, Class<? extends Channel3> channelClass
  ) {
    return new MoveThruChannel3Action<>(sourceType, channelClass);
  }

  static <S, T> Action1<T, S> mapOfMovingThruChannel0(
      Type<S> sourceType, Class<? extends Channel0> channelClass
  ) {
    return new MapOfMovingThruChannel0Action<>(sourceType, channelClass);
  }

  static <S, T, Q> Action2<T, S, Q> mapOfMovingThruChannel1(
      Type<S> sourceType, Class<? extends Channel1> channelClass
  ) {
    return new MapOfMovingThruChannel1Action<>(sourceType, channelClass);
  }

  static <S, T, Q1, Q2> Action3<T, S, Q1, Q2> mapOfMovingThruChannel2(
      Type<S> sourceType, Class<? extends Channel2> channelClass
  ) {
    return new MapOfMovingThruChannel2Action<>(sourceType, channelClass);
  }

  static <S, T, Q1, Q2, Q3> Action4<T, S, Q1, Q2, Q3> mapOfMovingThruChannel3(
      Type<S> sourceType, Class<? extends Channel3> channelClass
  ) {
    return new MapOfMovingThruChannel3Action<>(sourceType, channelClass);
  }
}
