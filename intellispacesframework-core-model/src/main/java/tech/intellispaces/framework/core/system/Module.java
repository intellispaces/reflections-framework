package tech.intellispaces.framework.core.system;

import tech.intellispaces.framework.core.guide.n0.Mover0;
import tech.intellispaces.framework.core.guide.n1.Mover1;

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

  <S, B> Mover0<S, B> autoMoverThruTransition0(Class<S> sourceClass, String tid);

  <S, B, Q> Mover1<S, B, Q> autoMoverThruTransition1(Class<S> sourceClass, String tid);
}
