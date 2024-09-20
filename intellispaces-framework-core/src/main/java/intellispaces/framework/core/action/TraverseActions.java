package intellispaces.framework.core.action;

import intellispaces.common.action.Action1;
import intellispaces.common.action.Action2;
import intellispaces.common.action.Action3;
import intellispaces.common.action.Action4;
import intellispaces.common.base.type.Type;
import intellispaces.framework.core.space.transition.Transition0;
import intellispaces.framework.core.space.transition.Transition1;
import intellispaces.framework.core.space.transition.Transition2;
import intellispaces.framework.core.space.transition.Transition3;

public interface TraverseActions {

  static <S, T> Action1<T, S> mapThruTransition0(
    Type<S> sourceType, Class<? extends Transition0> transitionClass
  ) {
    return new MapThruTransition0Action<>(sourceType, transitionClass);
  }

  static <S, T, Q> Action2<T, S, Q> mapThruTransition1(
    Type<S> sourceType, Class<? extends Transition1> transitionClass
  ) {
    return new MapThruTransition1Action<>(sourceType, transitionClass);
  }

  static <S, T, Q1, Q2> Action3<T, S, Q1, Q2> mapThruTransition2(
    Type<S> sourceType, Class<? extends Transition2> transitionClass
  ) {
    return new MapThruTransition2Action<>(sourceType, transitionClass);
  }

  static <S, T, Q1, Q2, Q3> Action4<T, S, Q1, Q2, Q3> mapThruTransition3(
    Type<S> sourceType, Class<? extends Transition3> transitionClass
  ) {
    return new MapThruTransition3Action<>(sourceType, transitionClass);
  }

  static <S, R> Action1<R, S> moveThruTransition0(
    Type<S> sourceType, Class<? extends Transition0> transitionClass
  ) {
    return new MoveThruTransition0Action<>(sourceType, transitionClass);
  }

  static <S, R, Q> Action2<R, S, Q> moveThruTransition1(
    Type<S> sourceType, Class<? extends Transition1> transitionClass
  ) {
    return new MoveThruTransition1Action<>(sourceType, transitionClass);
  }

  static <S, R, Q1, Q2> Action3<R, S, Q1, Q2> moveThruTransition2(
    Type<S> sourceType, Class<? extends Transition2> transitionClass
  ) {
    return new MoveThruTransition2Action<>(sourceType, transitionClass);
  }

  static <S, R, Q1, Q2, Q3> Action4<R, S, Q1, Q2, Q3> moveThruTransition3(
    Type<S> sourceType, Class<? extends Transition3> transitionClass
  ) {
    return new MoveThruTransition3Action<>(sourceType, transitionClass);
  }
}
