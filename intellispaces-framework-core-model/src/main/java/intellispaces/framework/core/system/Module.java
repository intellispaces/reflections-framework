package intellispaces.framework.core.system;

import intellispaces.common.base.type.Type;
import intellispaces.framework.core.guide.n0.Mapper0;
import intellispaces.framework.core.guide.n0.MapperOfMoving0;
import intellispaces.framework.core.guide.n0.Mover0;
import intellispaces.framework.core.guide.n1.Mapper1;
import intellispaces.framework.core.guide.n1.MapperOfMoving1;
import intellispaces.framework.core.guide.n1.Mover1;
import intellispaces.framework.core.guide.n2.Mapper2;
import intellispaces.framework.core.guide.n2.MapperOfMoving2;
import intellispaces.framework.core.guide.n2.Mover2;
import intellispaces.framework.core.guide.n3.Mapper3;
import intellispaces.framework.core.guide.n3.MapperOfMoving3;
import intellispaces.framework.core.guide.n3.Mover3;
import intellispaces.framework.core.space.transition.Transition0;
import intellispaces.framework.core.space.transition.Transition1;
import intellispaces.framework.core.space.transition.Transition2;
import intellispaces.framework.core.space.transition.Transition3;

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

  <S, R> R moveThruTransition0(S source, String tid);

  <S, R, Q> R moveThruTransition1(S source, String tid, Q qualifier);

  <S, T> Mapper0<S, T> autoMapperThruTransition0(Type<S> sourceType, String tid);

  <S, T, Q> Mapper1<S, T, Q> autoMapperThruTransition1(Type<S> sourceType, String tid);

  <S, T, Q1, Q2> Mapper2<S, T, Q1, Q2> autoMapperThruTransition2(Type<S> sourceType, String tid);

  <S, T, Q1, Q2, Q3> Mapper3<S, T, Q1, Q2, Q3> autoMapperThruTransition3(Type<S> sourceType, String tid);

  <S> Mover0<S> autoMoverThruTransition0(Type<S> sourceType, String tid);

  <S, Q> Mover1<S, Q> autoMoverThruTransition1(Class<S> sourceClass, String tid);

  <S, Q> Mover1<S, Q> autoMoverThruTransition1(Type<S> sourceType, String tid);

  <S, Q1, Q2> Mover2<S, Q1, Q2> autoMoverThruTransition2(Type<S> sourceType, String tid);

  <S, Q1, Q2, Q3> Mover3<S, Q1, Q2, Q3> autoMoverThruTransition3(Type<S> sourceType, String tid);

  <S, T> MapperOfMoving0<S, T> autoMapperOfMovingThruTransition0(Type<S> sourceType, String tid);

  <S, T, Q> MapperOfMoving1<S, T, Q> autoMapperOfMovingThruTransition1(Type<S> sourceType, String tid);

  <S, T, Q1, Q2> MapperOfMoving2<S, T, Q1, Q2> autoMapperOfMovingThruTransition2(Type<S> sourceType, String tid);

  <S, T, Q1, Q2, Q3> MapperOfMoving3<S, T, Q1, Q2, Q3> autoMapperOfMovingThruTransition3(Type<S> sourceType, String tid);

  @SuppressWarnings("rawtypes")
  <S, T> Mapper0<S, T> autoMapperThruTransition0(Type<S> sourceType, Class<? extends Transition0> transitionClass);

  @SuppressWarnings("rawtypes")
  <S, T, Q> Mapper1<S, T, Q> autoMapperThruTransition1(Type<S> sourceType, Class<? extends Transition1> transitionClass);

  @SuppressWarnings("rawtypes")
  <S, T, Q1, Q2> Mapper2<S, T, Q1, Q2> autoMapperThruTransition2(Type<S> sourceType, Class<? extends Transition2> transitionClass);

  @SuppressWarnings("rawtypes")
  <S, T, Q1, Q2, Q3> Mapper3<S, T, Q1, Q2, Q3> autoMapperThruTransition3(Type<S> sourceType, Class<? extends Transition3> transitionClass);

  @SuppressWarnings("rawtypes")
  <S> Mover0<S> autoMoverThruTransition0(Type<S> sourceType, Class<? extends Transition0> transitionClass);

  @SuppressWarnings("rawtypes")
  <S, Q> Mover1<S, Q> autoMoverThruTransition1(Type<S> sourceType, Class<? extends Transition1> transitionClass);

  @SuppressWarnings("rawtypes")
  <S, Q1, Q2> Mover2<S, Q1, Q2> autoMoverThruTransition2(Type<S> sourceType, Class<? extends Transition2> transitionClass);

  @SuppressWarnings("rawtypes")
  <S, Q1, Q2, Q3> Mover3<S, Q1, Q2, Q3> autoMoverThruTransition3(Type<S> sourceType, Class<? extends Transition3> transitionClass);

  @SuppressWarnings("rawtypes")
  <S, T> MapperOfMoving0<S, T> autoMapperOfMovingThruTransition0(Type<S> sourceType, Class<? extends Transition0> transitionClass);

  @SuppressWarnings("rawtypes")
  <S, T, Q> MapperOfMoving1<S, T, Q> autoMapperOfMovingThruTransition1(Type<S> sourceType, Class<? extends Transition1> transitionClass);

  @SuppressWarnings("rawtypes")
  <S, T, Q1, Q2> MapperOfMoving2<S, T, Q1, Q2> autoMapperOfMovingThruTransition2(Type<S> sourceType, Class<? extends Transition2> transitionClass);

  @SuppressWarnings("rawtypes")
  <S, T, Q1, Q2, Q3> MapperOfMoving3<S, T, Q1, Q2, Q3> autoMapperOfMovingThruTransition3(Type<S> sourceType, Class<? extends Transition3> transitionClass);
}
