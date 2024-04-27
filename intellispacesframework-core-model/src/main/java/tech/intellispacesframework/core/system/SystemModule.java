package tech.intellispacesframework.core.system;

import tech.intellispacesframework.core.guide.n1.Mover1;

import java.util.List;
import java.util.Optional;

public interface SystemModule {

  SystemModule start();

  void shutdown();

  boolean isStarted();

  List<SystemUnit> units();

  <T> Optional<T> projection(String name, Class<T> targetClass);

  <S, Q> Mover1<S, Q> autoMoverThruTransition1(Class<S> sourceClass, String tid);

  <S, Q> S moveThruTransition1(S source, String tid, Q qualifier);
}
