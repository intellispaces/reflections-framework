package tech.intellispaces.jaquarius.action;

import tech.intellispaces.jaquarius.channel.Channel0;
import tech.intellispaces.jaquarius.channel.Channel1;
import tech.intellispaces.jaquarius.channel.Channel2;
import tech.intellispaces.jaquarius.channel.Channel3;
import tech.intellispaces.jaquarius.channel.Channel4;
import tech.intellispaces.action.Action1;
import tech.intellispaces.action.Action2;
import tech.intellispaces.action.Action3;
import tech.intellispaces.action.Action4;
import tech.intellispaces.action.Action5;
import tech.intellispaces.entity.type.Type;
import tech.intellispaces.entity.type.Types;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForm;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForms;

public interface TraverseActions {

  static <S, T> Action1<T, S> mapThruChannel0(
      Class<S> sourceClass, Class<? extends Channel0> channelClass, ObjectReferenceForm targetForm
  ) {
    return new MapThruChannel0Action<>(Types.get(sourceClass), channelClass, targetForm);
  }

  static <S, T> Action1<T, S> mapThruChannel0(
    Type<S> sourceType, Class<? extends Channel0> channelClass, ObjectReferenceForm targetForm
  ) {
    return new MapThruChannel0Action<>(sourceType, channelClass, targetForm);
  }

  static <S, T> Action1<T, S> mapThruChannel0(
      Class<S> sourceClass, Class<? extends Channel0> channelClass
  ) {
    return new MapThruChannel0Action<>(Types.get(sourceClass), channelClass, ObjectReferenceForms.Object);
  }

  static <S, T, Q> Action2<T, S, Q> mapThruChannel1(
      Class<S> sourceClass, Class<? extends Channel1> channelClass, ObjectReferenceForm targetForm
  ) {
    return new MapThruChannel1Action<>(Types.get(sourceClass), channelClass, targetForm);
  }

  static <S, T, Q> Action2<T, S, Q> mapThruChannel1(
    Type<S> sourceType, Class<? extends Channel1> channelClass, ObjectReferenceForm targetForm
  ) {
    return new MapThruChannel1Action<>(sourceType, channelClass, targetForm);
  }

  static <S, T, Q> Action2<T, S, Q> mapThruChannel1(
      Class<S> sourceClass, Class<? extends Channel1> channelClass
  ) {
    return new MapThruChannel1Action<>(Types.get(sourceClass), channelClass, ObjectReferenceForms.Object);
  }

  static <S, T, Q1, Q2> Action3<T, S, Q1, Q2> mapThruChannel2(
      Class<S> sourceClass, Class<? extends Channel2> channelClass, ObjectReferenceForm targetForm
  ) {
    return new MapThruChannel2Action<>(Types.get(sourceClass), channelClass, targetForm);
  }

  static <S, T, Q1, Q2> Action3<T, S, Q1, Q2> mapThruChannel2(
    Type<S> sourceType, Class<? extends Channel2> channelClass, ObjectReferenceForm targetForm
  ) {
    return new MapThruChannel2Action<>(sourceType, channelClass, targetForm);
  }

  static <S, T, Q1, Q2, Q3> Action4<T, S, Q1, Q2, Q3> mapThruChannel3(
      Class<S> sourceClass, Class<? extends Channel3> channelClass, ObjectReferenceForm targetForm
  ) {
    return new MapThruChannel3Action<>(Types.get(sourceClass), channelClass, targetForm);
  }

  static <S, T, Q1, Q2, Q3> Action4<T, S, Q1, Q2, Q3> mapThruChannel3(
    Type<S> sourceType, Class<? extends Channel3> channelClass, ObjectReferenceForm targetForm
  ) {
    return new MapThruChannel3Action<>(sourceType, channelClass, targetForm);
  }

  static <S> Action1<S, S> moveThruChannel0(
      Class<S> sourceClass, Class<? extends Channel0> channelClass, ObjectReferenceForm targetForm
  ) {
    return new MoveThruChannel0Action<>(Types.get(sourceClass), channelClass, targetForm);
  }

  static <S> Action1<S, S> moveThruChannel0(
    Type<S> sourceType, Class<? extends Channel0> channelClass, ObjectReferenceForm targetForm
  ) {
    return new MoveThruChannel0Action<>(sourceType, channelClass, targetForm);
  }

  static <S, Q> Action2<S, S, Q> moveThruChannel1(
      Class<S> sourceClass, Class<? extends Channel1> channelClass, ObjectReferenceForm targetForm
  ) {
    return new MoveThruChannel1Action<>(Types.get(sourceClass), channelClass, targetForm);
  }

  static <S, Q> Action2<S, S, Q> moveThruChannel1(
    Type<S> sourceType, Class<? extends Channel1> channelClass, ObjectReferenceForm targetForm
  ) {
    return new MoveThruChannel1Action<>(sourceType, channelClass, targetForm);
  }

  static <S, Q1, Q2> Action3<S, S, Q1, Q2> moveThruChannel2(
      Class<S> sourceClass, Class<? extends Channel2> channelClass, ObjectReferenceForm targetForm
  ) {
    return new MoveThruChannel2Action<>(Types.get(sourceClass), channelClass, targetForm);
  }

  static <S, Q1, Q2> Action3<S, S, Q1, Q2> moveThruChannel2(
    Type<S> sourceType, Class<? extends Channel2> channelClass, ObjectReferenceForm targetForm
  ) {
    return new MoveThruChannel2Action<>(sourceType, channelClass, targetForm);
  }

  static <S, Q1, Q2, Q3> Action4<S, S, Q1, Q2, Q3> moveThruChannel3(
      Class<S> sourceClass, Class<? extends Channel3> channelClass, ObjectReferenceForm targetForm
  ) {
    return new MoveThruChannel3Action<>(Types.get(sourceClass), channelClass, targetForm);
  }

  static <S, Q1, Q2, Q3> Action4<S, S, Q1, Q2, Q3> moveThruChannel3(
    Type<S> sourceType, Class<? extends Channel3> channelClass, ObjectReferenceForm targetForm
  ) {
    return new MoveThruChannel3Action<>(sourceType, channelClass, targetForm);
  }

  static <S, T> Action1<T, S> mapOfMovingThruChannel0(
      Class<S> sourceClass, Class<? extends Channel0> channelClass, ObjectReferenceForm targetForm
  ) {
    return new MapOfMovingThruChannel0Action<>(Types.get(sourceClass), channelClass, targetForm);
  }

  static <S, T> Action1<T, S> mapOfMovingThruChannel0(
      Type<S> sourceType, Class<? extends Channel0> channelClass, ObjectReferenceForm targetForm
  ) {
    return new MapOfMovingThruChannel0Action<>(sourceType, channelClass, targetForm);
  }

  static <S, T, Q> Action2<T, S, Q> mapOfMovingThruChannel1(
      Class<S> sourceClass, Class<? extends Channel1> channelClass, ObjectReferenceForm targetForm
  ) {
    return new MapOfMovingThruChannel1Action<>(Types.get(sourceClass), channelClass, targetForm);
  }

  static <S, T, Q> Action2<T, S, Q> mapOfMovingThruChannel1(
      Type<S> sourceType, Class<? extends Channel1> channelClass, ObjectReferenceForm targetForm
  ) {
    return new MapOfMovingThruChannel1Action<>(sourceType, channelClass, targetForm);
  }

  static <S, T, Q> Action2<T, S, Q> mapOfMovingThruChannel1(
      Class<S> sourceClass, Class<? extends Channel1> channelClass
  ) {
    return new MapOfMovingThruChannel1Action<>(Types.get(sourceClass), channelClass, ObjectReferenceForms.Object);
  }

  static <S, T, Q1, Q2> Action3<T, S, Q1, Q2> mapOfMovingThruChannel2(
      Class<S> sourceClass, Class<? extends Channel2> channelClass, ObjectReferenceForm targetForm
  ) {
    return new MapOfMovingThruChannel2Action<>(Types.get(sourceClass), channelClass, targetForm);
  }

  static <S, T, Q1, Q2> Action3<T, S, Q1, Q2> mapOfMovingThruChannel2(
      Type<S> sourceType, Class<? extends Channel2> channelClass, ObjectReferenceForm targetForm
  ) {
    return new MapOfMovingThruChannel2Action<>(sourceType, channelClass, targetForm);
  }

  static <S, T, Q1, Q2, Q3> Action4<T, S, Q1, Q2, Q3> mapOfMovingThruChannel3(
      Class<S> sourceClass, Class<? extends Channel3> channelClass, ObjectReferenceForm targetForm
  ) {
    return new MapOfMovingThruChannel3Action<>(Types.get(sourceClass), channelClass, targetForm);
  }

  static <S, T, Q1, Q2, Q3> Action4<T, S, Q1, Q2, Q3> mapOfMovingThruChannel3(
      Type<S> sourceType, Class<? extends Channel3> channelClass, ObjectReferenceForm targetForm
  ) {
    return new MapOfMovingThruChannel3Action<>(sourceType, channelClass, targetForm);
  }

  static <S, T, Q1, Q2, Q3, Q4> Action5<T, S, Q1, Q2, Q3, Q4> mapOfMovingThruChannel4(
      Class<S> sourceClass, Class<? extends Channel4> channelClass, ObjectReferenceForm targetForm
  ) {
    return new MapOfMovingThruChannel4Action<>(Types.get(sourceClass), channelClass, targetForm);
  }

  static <S, T, Q1, Q2, Q3, Q4> Action5<T, S, Q1, Q2, Q3, Q4> mapOfMovingThruChannel4(
      Type<S> sourceType, Class<? extends Channel4> channelClass, ObjectReferenceForm targetForm
  ) {
    return new MapOfMovingThruChannel4Action<>(sourceType, channelClass, targetForm);
  }
}
