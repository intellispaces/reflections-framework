package intellispaces.core.system;

import intellispaces.core.guide.n0.Mapper0;
import intellispaces.core.guide.n0.Mover0;
import intellispaces.core.guide.n1.Mapper1;
import intellispaces.core.guide.n1.Mover1;
import intellispaces.core.guide.n2.Mapper2;
import intellispaces.core.guide.n2.Mover2;
import intellispaces.core.space.transition.Transition0;
import intellispaces.core.space.transition.Transition1;
import intellispaces.core.space.transition.Transition2;
import intellispaces.javastatements.type.Type;

import java.util.Collection;
import java.util.List;

/**
 * System module.
 */
public interface Module {

  default void start() {
    start(new String[] {});
  }

  void start(String[] args);

  void stop();

  <T> T getProjection(String name, Class<T> targetClass);

  <T> List<T> getProjections(Class<T> targetClass);

  Collection<ModuleProjection> allProjections();

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

  @SuppressWarnings("rawtypes")
  <S, T> Mapper0<S, T> autoMapperThruTransition0(Type<S> sourceType, Class<? extends Transition0> transitionClass);

  @SuppressWarnings("rawtypes")
  <S, T, Q> Mapper1<S, T, Q> autoMapperThruTransition1(Type<S> sourceType, Class<? extends Transition1> transitionClass);

  @SuppressWarnings("rawtypes")
  <S, T, Q1, Q2> Mapper2<S, T, Q1, Q2> autoMapperThruTransition2(Type<S> sourceType, Class<? extends Transition2> transitionClass);

  @SuppressWarnings("rawtypes")
  <S, B> Mover0<S, B> autoMoverThruTransition0(Type<S> sourceType, Class<? extends Transition0> transitionClass);

  @SuppressWarnings("rawtypes")
  <S, B, Q> Mover1<S, B, Q> autoMoverThruTransition1(Type<S> sourceType, Class<? extends Transition1> transitionClass);

  @SuppressWarnings("rawtypes")
  <S, B, Q1, Q2> Mover2<S, B, Q1, Q2> autoMoverThruTransition2(Type<S> sourceType, Class<? extends Transition2> transitionClass);
}
