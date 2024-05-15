package tech.intellispacesframework.core.system;

import tech.intellispacesframework.core.guide.n0.Mover0;
import tech.intellispacesframework.core.guide.n1.Mover1;

import java.util.Collection;
import java.util.List;

/**
 * System module.
 */
public interface Module {

  List<Unit> units();

  <T> T projection(String name, Class<T> targetClass);

  /**
   * Returns current loaded projections of module.
   */
  Collection<SystemProjection> projections();

  <S> Mover0<S> autoMoverThruTransition0(Class<S> sourceClass, String tid);

  <S, Q> Mover1<S, Q> autoMoverThruTransition1(Class<S> sourceClass, String tid);

  <S, Q> S moveThruTransition1(S source, String tid, Q qualifier);

  void shutdown();
}
