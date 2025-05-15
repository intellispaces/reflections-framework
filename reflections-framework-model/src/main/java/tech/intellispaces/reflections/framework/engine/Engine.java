package tech.intellispaces.reflections.framework.engine;

import java.util.List;

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
import tech.intellispaces.reflections.framework.channel.Channel0;
import tech.intellispaces.reflections.framework.channel.Channel1;
import tech.intellispaces.reflections.framework.engine.description.ReflectionImplementationMethod;
import tech.intellispaces.reflections.framework.engine.description.ReflectionImplementationType;
import tech.intellispaces.reflections.framework.engine.description.UnitMethod;
import tech.intellispaces.reflections.framework.system.Module;
import tech.intellispaces.reflections.framework.system.UnitWrapper;
import tech.intellispaces.reflections.framework.traverse.MappingOfMovingTraverse;
import tech.intellispaces.reflections.framework.traverse.MappingTraverse;

/**
 * Reflections engine.
 */
public interface Engine {

  Module createModule(List<Class<?>> unitClasses, String[] args);

  /**
   * Registers module unit.
   *
   * @param unitWrapper the unit wrapper instance.
   * @param unitClass the unit class.
   * @param methods unit wrapper method descriptions.
   * @return the unit broker.
   *
   * @param <U> the unit type.
   * @param <W> the unit wrapper type.
   */
  <U, W extends UnitWrapper> UnitBroker registerUnit(W unitWrapper, Class<U> unitClass, UnitMethod... methods);


  /**
   * Loads engine.
   *
   * @param unitClasses module unit classes.
   * @param args command line arguments.
   */
  void load(List<Class<?>> unitClasses, String[] args);

  void start();

  void start(String[] args);

  void stop();

  <S, T> T mapThruChannel0(S source, String cid);

  <S, T, C extends Channel0 & MappingTraverse> T mapThruChannel0(S source, Class<C> channelClass);

  <S, T, Q> T mapThruChannel1(S source, String cid, Q qualifier);

  <S, T, Q, C extends Channel1 & MappingTraverse> T mapThruChannel1(S source, Class<C> channelClass, Q qualifier);

  <S, R> R moveThruChannel0(S source, String cid);

  <S, R, Q> R moveThruChannel1(S source, String cid, Q qualifier);

  <S, R, Q, C extends Channel1 & MappingOfMovingTraverse> R mapOfMovingThruChannel1(S source, Class<C> channelClass, Q qualifier);

  <S, R, Q> R mapOfMovingThruChannel1(S source, String cid, Q qualifier);

  /**
   * Registers reflection implementation type.
   *
   * @param reflectionWrapperClass the reflection wrapper class.
   * @param reflectionImplClass the reflection implementation class.
   * @param methods reflection wrapper method descriptions.
   * @return the registered reflection wrapper type.
   *
   * @param <R> the reflection type.
   * @param <W> the reflection wrapper type.
   */
  <R, W extends R> ReflectionImplementationType registerReflectionImplementationType(
      Class<W> reflectionWrapperClass, Class<R> reflectionImplClass, ReflectionImplementationMethod... methods
  );

  /**
   * Registers reflection.
   *
   * @param reflectionWrapper the reflection wrapper.
   * @param type the reflection implementation type.
   * @return the reflection broker.
   *
   * @param <W> the reflection wrapper type.
   */
  <W> ReflectionBroker registerReflection(W reflectionWrapper, ReflectionImplementationType type);

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
