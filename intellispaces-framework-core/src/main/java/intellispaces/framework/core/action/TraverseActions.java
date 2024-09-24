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

  static <S> Action1<S, S> moveThruTransition0(
    Type<S> sourceType, Class<? extends Transition0> transitionClass
  ) {
    return new MoveThruTransition0Action<>(sourceType, transitionClass);
  }

  static <S, Q> Action2<S, S, Q> moveThruTransition1(
    Type<S> sourceType, Class<? extends Transition1> transitionClass
  ) {
    return new MoveThruTransition1Action<>(sourceType, transitionClass);
  }

  static <S, Q1, Q2> Action3<S, S, Q1, Q2> moveThruTransition2(
    Type<S> sourceType, Class<? extends Transition2> transitionClass
  ) {
    return new MoveThruTransition2Action<>(sourceType, transitionClass);
  }

  static <S, Q1, Q2, Q3> Action4<S, S, Q1, Q2, Q3> moveThruTransition3(
    Type<S> sourceType, Class<? extends Transition3> transitionClass
  ) {
    return new MoveThruTransition3Action<>(sourceType, transitionClass);
  }

  static <S, T> Action1<T, S> mapOfMovingThruTransition0(
      Type<S> sourceType, Class<? extends Transition0> transitionClass
  ) {
    return new MapOfMovingThruTransition0Action<>(sourceType, transitionClass);
  }

  static <S, T, Q> Action2<T, S, Q> mapOfMovingThruTransition1(
      Type<S> sourceType, Class<? extends Transition1> transitionClass
  ) {
    return new MapOfMovingThruTransition1Action<>(sourceType, transitionClass);
  }

  static <S, T, Q1, Q2> Action3<T, S, Q1, Q2> mapOfMovingThruTransition2(
      Type<S> sourceType, Class<? extends Transition2> transitionClass
  ) {
    return new MapOfMovingThruTransition2Action<>(sourceType, transitionClass);
  }

  static <S, T, Q1, Q2, Q3> Action4<T, S, Q1, Q2, Q3> mapOfMovingThruTransition3(
      Type<S> sourceType, Class<? extends Transition3> transitionClass
  ) {
    return new MapOfMovingThruTransition3Action<>(sourceType, transitionClass);
  }
}
