package tech.intellispacesframework.core.system;

import tech.intellispacesframework.core.guide.n0.Mover0;
import tech.intellispacesframework.core.guide.n1.Mover1;

/**
 * System module.
 */
public interface Module {

  default void start() {
    start(new String[] {});
  }

  void start(String[] args);

  void shutdown();

  <S> Mover0<S> autoMoverThruTransition0(Class<S> sourceClass, String tid);

  <S, Q> Mover1<S, Q> autoMoverThruTransition1(Class<S> sourceClass, String tid);

  <S, T> T mapThruTransition0(S source, String tid);

  <S, T, Q> T mapThruTransition1(S source, String tid, Q qualifier);

  <S> S moveThruTransition0(S source, String tid);

  <S, Q> S moveThruTransition1(S source, String tid, Q qualifier);
}
