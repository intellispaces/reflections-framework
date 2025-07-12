package tech.intellispaces.reflections.framework.engine;

import java.util.Collection;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import tech.intellispaces.actions.Action0;
import tech.intellispaces.actions.Action1;
import tech.intellispaces.actions.Action10;
import tech.intellispaces.actions.Action2;
import tech.intellispaces.actions.Action3;
import tech.intellispaces.actions.Action4;
import tech.intellispaces.actions.Action5;
import tech.intellispaces.actions.Action6;
import tech.intellispaces.actions.Action7;
import tech.intellispaces.actions.Action8;
import tech.intellispaces.actions.Action9;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.core.Reflection;
import tech.intellispaces.core.ReflectionContract;
import tech.intellispaces.core.ReflectionDomain;
import tech.intellispaces.core.ReflectionPoint;
import tech.intellispaces.core.Rid;
import tech.intellispaces.core.TraversableReflection;
import tech.intellispaces.core.TraversableReflectionPoint;
import tech.intellispaces.reflections.framework.channel.Channel0;
import tech.intellispaces.reflections.framework.channel.Channel1;
import tech.intellispaces.reflections.framework.channel.Channel2;
import tech.intellispaces.reflections.framework.channel.Channel3;
import tech.intellispaces.reflections.framework.channel.Channel4;
import tech.intellispaces.reflections.framework.guide.SystemGuide;
import tech.intellispaces.reflections.framework.guide.n0.Mapper0;
import tech.intellispaces.reflections.framework.guide.n0.MapperOfMoving0;
import tech.intellispaces.reflections.framework.guide.n0.Mover0;
import tech.intellispaces.reflections.framework.guide.n1.Mapper1;
import tech.intellispaces.reflections.framework.guide.n1.MapperOfMoving1;
import tech.intellispaces.reflections.framework.guide.n1.Mover1;
import tech.intellispaces.reflections.framework.guide.n2.Mapper2;
import tech.intellispaces.reflections.framework.guide.n2.MapperOfMoving2;
import tech.intellispaces.reflections.framework.guide.n2.Mover2;
import tech.intellispaces.reflections.framework.guide.n3.Mapper3;
import tech.intellispaces.reflections.framework.guide.n3.MapperOfMoving3;
import tech.intellispaces.reflections.framework.guide.n3.Mover3;
import tech.intellispaces.reflections.framework.guide.n4.MapperOfMoving4;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.reflection.ReflectionHandle;
import tech.intellispaces.reflections.framework.reflection.ReflectionRealizationType;
import tech.intellispaces.reflections.framework.system.ModuleProjection;
import tech.intellispaces.reflections.framework.system.ProjectionDefinition;
import tech.intellispaces.reflections.framework.system.ReflectionFactory;
import tech.intellispaces.reflections.framework.traverse.MappingOfMovingTraverse;
import tech.intellispaces.reflections.framework.traverse.MappingTraverse;

/**
 * Reflections engine.
 */
public interface Engine {

  void start();

  void start(String[] args);

  void stop();

  <S, T> T mapSourceTo(S source, ReflectionDomain domain);

  TraversableReflectionPoint mapSourceTo(Reflection source, ReflectionDomain targetDomain);

  <R extends Reflection> R mapAndCastSourceTo(Reflection source, ReflectionDomain targetDomain, Class<R> targetClass);

  <Q, R extends Reflection> R mapAndCastSourceTo(Reflection source, ReflectionDomain targetDomain, Q qualifier, Class<R> targetClass);

  <S, T> T mapThruChannel0(S source, Rid cid);

  <S, T, C extends Channel0 & MappingTraverse> T mapThruChannel0(S source, Class<C> channelClass);

  <S, T, Q> T mapThruChannel1(S source, Rid cid, Q qualifier);

  <S, T, Q, C extends Channel1 & MappingTraverse> T mapThruChannel1(S source, Class<C> channelClass, Q qualifier);

  <S, R> R moveThruChannel0(S source, Rid cid);

  <S, R, Q> R moveThruChannel1(S source, Rid cid, Q qualifier);

  <S, R, Q, C extends Channel1 & MappingOfMovingTraverse> R mapOfMovingThruChannel1(S source, Class<C> channelClass, Q qualifier);

  <S, R, Q> R mapOfMovingThruChannel1(S source, Rid cid, Q qualifier);

  /**
   * Adds module projection.
   *
   * @param projectionDefinition the projection definition.
   */
  void addProjection(ProjectionDefinition projectionDefinition);

  <T> List<T> findProjections(Class<T> targetReflectionClass);

  /**
   * Adds guide.
   *
   * @param guide the guide.
   */
  void addGuide(SystemGuide<?, ?> guide);

  /**
   * Searches for a reflection by name in the system.
   *
   * @param reflectionName the qualified reflection name.
   * @return the reflection or <code>null</code> if reflection is not found.
   */
  @Nullable TraversableReflection getReflection(String reflectionName);

  /**
   * Searches for a reflection by reflection identifier and domain name in the system.
   *
   * @param pid the reflection identifier.
   * @param domainName the reflection domain name.
   * @return the reflection or <code>null</code> if reflection is not found.
   */
  @Nullable TraversableReflectionPoint getReflection(Rid pid, String domainName);

  <T> T castReflection(ReflectionPoint reflection, Class<T> reflectionClass);

  /**
   * Creates and registers new reflection.
   *
   * @param contract the reflection contract.
   * @return the created reflection.
   */
  TraversableReflectionPoint createReflection(ReflectionContract contract);

  /**
   * Searches for factories that create reflections of a given domain.
   *
   * @param domain the reflection domain.
   * @return the list of factories.
   */
  List<ReflectionFactory> findFactories(ReflectionDomain domain);

  <S, T> Mapper0<S, T> autoMapperThruChannel0(Type<S> sourceType, Rid cid, ReflectionForm targetForm);

  <S, T, Q> Mapper1<S, T, Q> autoMapperThruChannel1(Type<S> sourceType, Rid cid, ReflectionForm targetForm);

  <S, T, Q1, Q2> Mapper2<S, T, Q1, Q2> autoMapperThruChannel2(Type<S> sourceType, Rid cid, ReflectionForm targetForm);

  <S, T, Q1, Q2, Q3> Mapper3<S, T, Q1, Q2, Q3> autoMapperThruChannel3(Type<S> sourceType, Rid cid, ReflectionForm targetForm);

  <S> Mover0<S> autoMoverThruChannel0(Type<S> sourceType, Rid cid, ReflectionForm targetForm);

  <S, Q> Mover1<S, Q> autoMoverThruChannel1(Class<S> sourceClass, Rid cid, ReflectionForm targetForm);

  <S, Q> Mover1<S, Q> autoMoverThruChannel1(Type<S> sourceType, Rid cid, ReflectionForm targetForm);

  <S, Q1, Q2> Mover2<S, Q1, Q2> autoMoverThruChannel2(Type<S> sourceType, Rid cid, ReflectionForm targetForm);

  <S, Q1, Q2, Q3> Mover3<S, Q1, Q2, Q3> autoMoverThruChannel3(Type<S> sourceType, Rid cid, ReflectionForm targetForm);

  <S, T> MapperOfMoving0<S, T> autoMapperOfMovingThruChannel0(Type<S> sourceType, Rid cid, ReflectionForm targetForm);

  <S, T, Q> MapperOfMoving1<S, T, Q> autoMapperOfMovingThruChannel1(Type<S> sourceType, Rid cid, ReflectionForm targetForm);

  <S, T, Q1, Q2> MapperOfMoving2<S, T, Q1, Q2> autoMapperOfMovingThruChannel2(Type<S> sourceType, Rid cid, ReflectionForm targetForm);

  <S, T, Q1, Q2, Q3> MapperOfMoving3<S, T, Q1, Q2, Q3> autoMapperOfMovingThruChannel3(Type<S> sourceType, Rid cid, ReflectionForm targetForm);

  <S, T, Q1, Q2, Q3, Q4> MapperOfMoving4<S, T, Q1, Q2, Q3, Q4> autoMapperOfMovingThruChannel4(Type<S> sourceType, Rid cid, ReflectionForm targetForm);

  <S, T> Mapper0<S, T> autoMapperThruChannel0(Type<S> sourceType, Class<? extends Channel0> channelClass, ReflectionForm targetForm);

  <S, T, Q> Mapper1<S, T, Q> autoMapperThruChannel1(Type<S> sourceType, Class<? extends Channel1> channelClass, ReflectionForm targetForm);

  <S, T, Q1, Q2> Mapper2<S, T, Q1, Q2> autoMapperThruChannel2(Type<S> sourceType, Class<? extends Channel2> channelClass, ReflectionForm targetForm);

  <S, T, Q1, Q2, Q3> Mapper3<S, T, Q1, Q2, Q3> autoMapperThruChannel3(Type<S> sourceType, Class<? extends Channel3> channelClass, ReflectionForm targetForm);

  <S> Mover0<S> autoMoverThruChannel0(Type<S> sourceType, Class<? extends Channel0> channelClass, ReflectionForm targetForm);

  <S, Q> Mover1<S, Q> autoMoverThruChannel1(Type<S> sourceType, Class<? extends Channel1> channelClass, ReflectionForm targetForm);

  <S, Q1, Q2> Mover2<S, Q1, Q2> autoMoverThruChannel2(Type<S> sourceType, Class<? extends Channel2> channelClass, ReflectionForm targetForm);

  <S, Q1, Q2, Q3> Mover3<S, Q1, Q2, Q3> autoMoverThruChannel3(Type<S> sourceType, Class<? extends Channel3> channelClass, ReflectionForm targetForm);

  <S, T> MapperOfMoving0<S, T> autoMapperOfMovingThruChannel0(Type<S> sourceType, Class<? extends Channel0> channelClass, ReflectionForm targetForm);

  <S, T, Q> MapperOfMoving1<S, T, Q> autoMapperOfMovingThruChannel1(Type<S> sourceType, Class<? extends Channel1> channelClass, ReflectionForm targetForm);

  <S, T, Q1, Q2> MapperOfMoving2<S, T, Q1, Q2> autoMapperOfMovingThruChannel2(Type<S> sourceType, Class<? extends Channel2> channelClass, ReflectionForm targetForm);

  <S, T, Q1, Q2, Q3> MapperOfMoving3<S, T, Q1, Q2, Q3> autoMapperOfMovingThruChannel3(Type<S> sourceType, Class<? extends Channel3> channelClass, ReflectionForm targetForm);

  <S, T, Q1, Q2, Q3, Q4> MapperOfMoving4<S, T, Q1, Q2, Q3, Q4> autoMapperOfMovingThruChannel4(Type<S> sourceType, Class<? extends Channel4> channelClass, ReflectionForm targetForm);

  List<tech.intellispaces.core.Guide> guides();

  <G> List<G> guides(Class<G> guideClass);

  <G> G getAutoGuide(Class<G> guideClass);

  <T> T getProjection(String name, Class<T> targetReflectionClass);

  <T> List<T> getProjections(Class<T> targetReflectionClass);

  Collection<ModuleProjection> moduleProjections();

  /**
   * Adds context projection.
   *
   * @param name the projection name.
   * @param targetReflectionClass the projection target class.
   * @param target the projection target.
   * @param <T> the projection target type.
   */
  <T> void addContextProjection(String name, Class<T> targetReflectionClass, T target);

  /**
   * Removes context projection.
   *
   * @param name the projection name.
   */
  void removeContextProjection(String name);

  /**
   * Registers reflection realization type.
   *
   * @param type the reflection realization type.
   */
  void registerReflectionRealizationType(ReflectionRealizationType type);

  /**
   * Registers reflection.
   *
   * @param reflection the reflection.
   * @param type the reflection realization type.
   * @return the reflection handle.
   *
   * @param <R> the reflection wrapper type.
   */
  <R> ReflectionHandle registerReflection(R reflection, ReflectionRealizationType type);

  /**
   * Returns the factory action for creating a reflection according to a contract type.
   *
   * @param targetDomainClass the required target domain.
   * @param contractType the factory contract type.
   * @param targetReflectionType the required target reflection type.
   * @return the factory action.
   * @param <R> the required target reflection type.
   */
  <R> Action0<R> getFactoryAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<R> targetReflectionType
  );

  /**
   * Returns the factory action for creating a reflection according to a contract type.
   *
   * @param targetDomainClass the required target domain.
   * @param contractType the factory contract type.
   * @param contractQualifierType the contract qualifier type.
   * @param targetReflectionType the required target reflection type.
   * @return the factory action.
   * @param <R> the required target reflection type.
   * @param <Q> the contract qualifier type.
   */
  <R, Q> Action1<R, Q> getFactoryAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q> contractQualifierType,
      Type<R> targetReflectionType
  );

  /**
   * Returns the factory action for creating a reflection according to a contract type.
   *
   * @param targetDomainClass the required target domain.
   * @param contractType the factory contract type.
   * @param contractQualifierType1 the first contract qualifier type.
   * @param contractQualifierType2 the second contract qualifier type.
   * @param targetReflectionType the required target reflection type.
   * @return the factory action.
   * @param <R> the required target reflection type.
   * @param <Q1> the first contract qualifier type.
   * @param <Q2> the second contract qualifier type.
   */
  <R, Q1, Q2> Action2<R, Q1, Q2> getFactoryAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<R> targetReflectionType
  );

  /**
   * Returns the factory action for creating a reflection according to a contract type.
   *
   * @param targetDomainClass the required target domain.
   * @param contractType the factory contract type.
   * @param contractQualifierType1 the first contract qualifier type.
   * @param contractQualifierType2 the second contract qualifier type.
   * @param contractQualifierType3 the third contract qualifier type.
   * @param targetReflectionType the required target reflection type.
   * @return the factory action.
   * @param <R> the required target reflection type.
   * @param <Q1> the first contract qualifier type.
   * @param <Q2> the second contract qualifier type.
   * @param <Q3> the third contract qualifier type.
   */
  <R, Q1, Q2, Q3> Action3<R, Q1, Q2, Q3> getFactoryAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<R> targetReflectionType
  );

  /**
   * Returns the factory action for creating a reflection according to a contract type.
   *
   * @param targetDomainClass the required target domain.
   * @param contractType the factory contract type.
   * @param contractQualifierType1 the first contract qualifier type.
   * @param contractQualifierType2 the second contract qualifier type.
   * @param contractQualifierType3 the third contract qualifier type.
   * @param contractQualifierType4 the fourth contract qualifier type.
   * @param targetReflectionType the required target reflection type.
   * @return the factory action.
   * @param <R> the required target reflection type.
   * @param <Q1> the first contract qualifier type.
   * @param <Q2> the second contract qualifier type.
   * @param <Q3> the third contract qualifier type.
   * @param <Q4> the fourth contract qualifier type.
   */
  <R, Q1, Q2, Q3, Q4> Action4<R, Q1, Q2, Q3, Q4> getFactoryAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<Q4> contractQualifierType4,
      Type<R> targetReflectionType
  );

  /**
   * Returns the factory action for creating a reflection according to a contract type.
   *
   * @param targetDomainClass the required target domain.
   * @param contractType the factory contract type.
   * @param contractQualifierType1 the first contract qualifier type.
   * @param contractQualifierType2 the second contract qualifier type.
   * @param contractQualifierType3 the third contract qualifier type.
   * @param contractQualifierType4 the fourth contract qualifier type.
   * @param contractQualifierType5 the fifth contract qualifier type.
   * @param targetReflectionType the required target reflection type.
   * @return the factory action.
   * @param <R> the required target reflection type.
   * @param <Q1> the first contract qualifier type.
   * @param <Q2> the second contract qualifier type.
   * @param <Q3> the third contract qualifier type.
   * @param <Q4> the fourth contract qualifier type.
   * @param <Q5> the fifth contract qualifier type.
   */
  <R, Q1, Q2, Q3, Q4, Q5> Action5<R, Q1, Q2, Q3, Q4, Q5> getFactoryAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<Q4> contractQualifierType4,
      Type<Q5> contractQualifierType5,
      Type<R> targetReflectionType
  );

  /**
   * Returns the factory action for creating a reflection according to a contract type.
   *
   * @param targetDomainClass the required target domain.
   * @param contractType the factory contract type.
   * @param contractQualifierType1 the first contract qualifier type.
   * @param contractQualifierType2 the second contract qualifier type.
   * @param contractQualifierType3 the third contract qualifier type.
   * @param contractQualifierType4 the fourth contract qualifier type.
   * @param contractQualifierType5 the fifth contract qualifier type.
   * @param contractQualifierType6 the sixth contract qualifier type.
   * @param targetReflectionType the required target reflection type.
   * @return the factory action.
   * @param <R> the required target reflection type.
   * @param <Q1> the first contract qualifier type.
   * @param <Q2> the second contract qualifier type.
   * @param <Q3> the third contract qualifier type.
   * @param <Q4> the fourth contract qualifier type.
   * @param <Q5> the fifth contract qualifier type.
   * @param <Q6> the sixth contract qualifier type.
   */
  <R, Q1, Q2, Q3, Q4, Q5, Q6> Action6<R, Q1, Q2, Q3, Q4, Q5, Q6> getFactoryAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<Q4> contractQualifierType4,
      Type<Q5> contractQualifierType5,
      Type<Q6> contractQualifierType6,
      Type<R> targetReflectionType
  );

  /**
   * Returns the factory action for creating a reflection according to a contract type.
   *
   * @param targetDomainClass the required target domain.
   * @param contractType the factory contract type.
   * @param contractQualifierType1 the first contract qualifier type.
   * @param contractQualifierType2 the second contract qualifier type.
   * @param contractQualifierType3 the third contract qualifier type.
   * @param contractQualifierType4 the fourth contract qualifier type.
   * @param contractQualifierType5 the fifth contract qualifier type.
   * @param contractQualifierType6 the sixth contract qualifier type.
   * @param contractQualifierType7 the seventh contract qualifier type.
   * @param targetReflectionType the required target reflection type.
   * @return the factory action.
   * @param <R> the required target reflection type.
   * @param <Q1> the first contract qualifier type.
   * @param <Q2> the second contract qualifier type.
   * @param <Q3> the third contract qualifier type.
   * @param <Q4> the fourth contract qualifier type.
   * @param <Q5> the fifth contract qualifier type.
   * @param <Q6> the sixth contract qualifier type.
   * @param <Q7> the seventh contract qualifier type.
   */
  <R, Q1, Q2, Q3, Q4, Q5, Q6, Q7> Action7<R, Q1, Q2, Q3, Q4, Q5, Q6, Q7> getFactoryAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<Q4> contractQualifierType4,
      Type<Q5> contractQualifierType5,
      Type<Q6> contractQualifierType6,
      Type<Q7> contractQualifierType7,
      Type<R> targetReflectionType
  );

  /**
   * Returns the factory action for creating a reflection according to a contract type.
   *
   * @param targetDomainClass the required target domain.
   * @param contractType the factory contract type.
   * @param contractQualifierType1 the first contract qualifier type.
   * @param contractQualifierType2 the second contract qualifier type.
   * @param contractQualifierType3 the third contract qualifier type.
   * @param contractQualifierType4 the fourth contract qualifier type.
   * @param contractQualifierType5 the fifth contract qualifier type.
   * @param contractQualifierType6 the sixth contract qualifier type.
   * @param contractQualifierType7 the seventh contract qualifier type.
   * @param contractQualifierType8 the eighth contract qualifier type.
   * @param targetReflectionType the required target reflection type.
   * @return the factory action.
   * @param <R> the required target reflection type.
   * @param <Q1> the first contract qualifier type.
   * @param <Q2> the second contract qualifier type.
   * @param <Q3> the third contract qualifier type.
   * @param <Q4> the fourth contract qualifier type.
   * @param <Q5> the fifth contract qualifier type.
   * @param <Q6> the sixth contract qualifier type.
   * @param <Q7> the seventh contract qualifier type.
   * @param <Q8> the eighth contract qualifier type.
   */
  <R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8> Action8<R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8> getFactoryAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<Q4> contractQualifierType4,
      Type<Q5> contractQualifierType5,
      Type<Q6> contractQualifierType6,
      Type<Q7> contractQualifierType7,
      Type<Q8> contractQualifierType8,
      Type<R> targetReflectionType
  );

  /**
   * Returns the factory action for creating a reflection according to a contract type.
   *
   * @param targetDomainClass the required target domain.
   * @param contractType the factory contract type.
   * @param contractQualifierType1 the first contract qualifier type.
   * @param contractQualifierType2 the second contract qualifier type.
   * @param contractQualifierType3 the third contract qualifier type.
   * @param contractQualifierType4 the fourth contract qualifier type.
   * @param contractQualifierType5 the fifth contract qualifier type.
   * @param contractQualifierType6 the sixth contract qualifier type.
   * @param contractQualifierType7 the seventh contract qualifier type.
   * @param contractQualifierType8 the eighth contract qualifier type.
   * @param contractQualifierType9 the ninth contract qualifier type.
   * @param targetReflectionType the required target reflection type.
   * @return the factory action.
   * @param <R> the required target reflection type.
   * @param <Q1> the first contract qualifier type.
   * @param <Q2> the second contract qualifier type.
   * @param <Q3> the third contract qualifier type.
   * @param <Q4> the fourth contract qualifier type.
   * @param <Q5> the fifth contract qualifier type.
   * @param <Q6> the sixth contract qualifier type.
   * @param <Q7> the seventh contract qualifier type.
   * @param <Q8> the eighth contract qualifier type.
   * @param <Q9> the ninth contract qualifier type.
   */
  <R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9> Action9<R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9> getFactoryAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<Q4> contractQualifierType4,
      Type<Q5> contractQualifierType5,
      Type<Q6> contractQualifierType6,
      Type<Q7> contractQualifierType7,
      Type<Q8> contractQualifierType8,
      Type<Q9> contractQualifierType9,
      Type<R> targetReflectionType
  );

  /**
   * Returns the factory action for creating a reflection according to a contract type.
   *
   * @param targetDomainClass the required target domain.
   * @param contractType the factory contract type.
   * @param contractQualifierType1 the first contract qualifier type.
   * @param contractQualifierType2 the second contract qualifier type.
   * @param contractQualifierType3 the third contract qualifier type.
   * @param contractQualifierType4 the fourth contract qualifier type.
   * @param contractQualifierType5 the fifth contract qualifier type.
   * @param contractQualifierType6 the sixth contract qualifier type.
   * @param contractQualifierType7 the seventh contract qualifier type.
   * @param contractQualifierType8 the eighth contract qualifier type.
   * @param contractQualifierType9 the ninth contract qualifier type.
   * @param contractQualifierType10 the tenth contract qualifier type.
   * @param targetReflectionType the required target reflection type.
   * @return the factory action.
   * @param <R> the required target reflection type.
   * @param <Q1> the first contract qualifier type.
   * @param <Q2> the second contract qualifier type.
   * @param <Q3> the third contract qualifier type.
   * @param <Q4> the fourth contract qualifier type.
   * @param <Q5> the fifth contract qualifier type.
   * @param <Q6> the sixth contract qualifier type.
   * @param <Q7> the seventh contract qualifier type.
   * @param <Q8> the eighth contract qualifier type.
   * @param <Q9> the ninth contract qualifier type.
   * @param <Q10> the tenth contract qualifier type.
   */
  <R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10> Action10<R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10> getFactoryAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<Q4> contractQualifierType4,
      Type<Q5> contractQualifierType5,
      Type<Q6> contractQualifierType6,
      Type<Q7> contractQualifierType7,
      Type<Q8> contractQualifierType8,
      Type<Q9> contractQualifierType9,
      Type<Q10> contractQualifierType10,
      Type<R> targetReflectionType
  );
}
