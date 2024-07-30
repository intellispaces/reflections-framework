package tech.intellispaces.core.system;

import tech.intellispaces.core.guide.n0.Mapper0;
import tech.intellispaces.core.guide.n0.Mover0;
import tech.intellispaces.core.guide.n1.Mapper1;
import tech.intellispaces.core.guide.n1.Mover1;
import tech.intellispaces.core.guide.n2.Mapper2;
import tech.intellispaces.core.guide.n2.Mover2;
import tech.intellispaces.core.space.transition.Transition0;
import tech.intellispaces.core.space.transition.Transition1;
import tech.intellispaces.core.space.transition.Transition2;
import tech.intellispaces.javastatements.type.Type;

/**
 * System module.
 */
public interface Module {

  default void start() {
    start(new String[] {});
  }

  void start(String[] args);

  void stop();

  <S, T> T mapThruTransition0(S source, String tid);

  <S, T, Q> T mapThruTransition1(S source, String tid, Q qualifier);

  <S, B> B moveThruTransition0(S source, String tid);

  <S, B, Q> B moveThruTransition1(S source, String tid, Q qualifier);

  <S, T> Mapper0<S, T> autoMapperThruTransition0(Type<S> sourceType, String tid);

  <S, T, Q> Mapper1<S, T, Q> autoMapperThruTransition1(Type<S> sourceType, String tid);

  <S, T, Q1, Q2> Mapper2<S, T, Q1, Q2> autoMapperThruTransition2(Type<S> sourceType, String tid);

  <S, B> Mover0<S, B> autoMoverThruTransition0(Type<S> sourceType, String tid);

  <S, B, Q> Mover1<S, B, Q> autoMoverThruTransition1(Type<S> sourceType, String tid);

  <S, B, Q1, Q2> Mover2<S, B, Q1, Q2> autoMoverThruTransition2(Type<S> sourceType, String tid);

  <S, T> Mapper0<S, T> autoMapperThruTransition0(Type<S> sourceType, Class<? extends Transition0<?, ?>> transitionClass);

  <S, T, Q> Mapper1<S, T, Q> autoMapperThruTransition1(Type<S> sourceType, Class<? extends Transition1<?, ?, ?>> transitionClass);

  <S, T, Q1, Q2> Mapper2<S, T, Q1, Q2> autoMapperThruTransition2(Type<S> sourceType, Class<? extends Transition2<?, ?, ?, ?>> transitionClass);

  <S, B> Mover0<S, B> autoMoverThruTransition0(Type<S> sourceType, Class<? extends Transition0<?, ?>> transitionClass);

  <S, B, Q> Mover1<S, B, Q> autoMoverThruTransition1(Type<S> sourceType, Class<? extends Transition1<?, ?, ?>> transitionClass);

  <S, B, Q1, Q2> Mover2<S, B, Q1, Q2> autoMoverThruTransition2(Type<S> sourceType, Class<? extends Transition2<?, ?, ?, ?>> transitionClass);
}
