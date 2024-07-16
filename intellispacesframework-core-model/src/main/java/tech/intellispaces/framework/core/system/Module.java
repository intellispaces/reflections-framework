package tech.intellispaces.framework.core.system;

import tech.intellispaces.framework.commons.type.Type;
import tech.intellispaces.framework.core.guide.n0.Mapper0;
import tech.intellispaces.framework.core.guide.n0.Mover0;
import tech.intellispaces.framework.core.guide.n1.Mapper1;
import tech.intellispaces.framework.core.guide.n1.Mover1;
import tech.intellispaces.framework.core.guide.n2.Mapper2;
import tech.intellispaces.framework.core.guide.n2.Mover2;
import tech.intellispaces.framework.core.space.transition.Transition0;
import tech.intellispaces.framework.core.space.transition.Transition1;
import tech.intellispaces.framework.core.space.transition.Transition2;

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

  <S, B> Mover0<S, B> autoMoverThruTransition0(Type<S> sourceType, String tid);

  <S, B, TRANSITION extends Transition0<? super S, ? super S>> Mover0<S, B> autoMoverThruTransition0(
      Type<S> sourceType, Class<TRANSITION> transitionClass
  );

  <S, B, Q> Mover1<S, B, Q> autoMoverThruTransition1(Type<S> sourceType, String tid);

  <S, B, Q, TRANSITION extends Transition1<? super S, ? super S, ? super Q>> Mover1<S, B, Q> autoMoverThruTransition1(
      Type<S> sourceType, Class<TRANSITION> transitionClass
  );

  <S, B, Q1, Q2> Mover2<S, B, Q1, Q2> autoMoverThruTransition2(Type<S> sourceType, String tid);

  <S, B, Q1, Q2, TRANSITION extends Transition2<? super S, ? super S, ? super Q1, ? super Q2>> Mover2<S, B, Q1, Q2> autoMoverThruTransition2(
      Type<S> sourceType, Class<TRANSITION> transitionClass
  );

  <S, T> Mapper0<S, T> autoMapperThruTransition0(Type<S> sourceType, String tid);

  <S, T, TRANSITION extends Transition0<? super S, ? super T>> Mapper0<S, T> autoMapperThruTransition0(
      Type<S> sourceType, Class<TRANSITION> transitionClass
  );

  <S, T, Q> Mapper1<S, T, Q> autoMapperThruTransition1(Type<S> sourceType, String tid);

  <S, T, Q, TRANSITION extends Transition1<? super S, ? super T, ? super Q>> Mapper1<S, T, Q> autoMapperThruTransition1(
      Type<S> sourceType, Class<TRANSITION> transitionClass
  );

  <S, T, Q1, Q2> Mapper2<S, T, Q1, Q2> autoMapperThruTransition2(Type<S> sourceType, String tid);

  <S, T, Q1, Q2, TRANSITION extends Transition2<? super S, ? super T, ? super Q1, ? super Q2>> Mapper2<S, T, Q1, Q2> autoMapperThruTransition2(
      Type<S> sourceType, Class<TRANSITION> transitionClass
  );
}
