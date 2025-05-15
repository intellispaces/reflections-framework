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
import tech.intellispaces.reflections.framework.engine.description.ReflectionImplementationMethodDescription;
import tech.intellispaces.reflections.framework.engine.description.ReflectionImplementationType;
import tech.intellispaces.reflections.framework.engine.description.UnitMethodDescription;
import tech.intellispaces.reflections.framework.system.Module;
import tech.intellispaces.reflections.framework.system.UnitWrapper;

/**
 * Reflections engine.
 */
public interface Engine {

  /**
   * Creates system module and load into current application.<p/>
   *
   * Only one module can be loaded in application.
   * If the module is already loaded in application, it will be unloaded.
   *
   * @param unitClasses module unit classes.
   * @param args command line arguments.
   */
  Module createModule(List<Class<?>> unitClasses, String[] args);

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
      Class<W> reflectionWrapperClass, Class<R> reflectionImplClass, ReflectionImplementationMethodDescription... methods
  );

  /**
   * Registers reflection.
   *
   * @param reflectionWrapper the reflection wrapper.
   * @param type the reflection type.
   * @return the reflection broker.
   *
   * @param <W> the reflection wrapper type.
   */
  <W> ReflectionBroker registerReflection(W reflectionWrapper, ReflectionImplementationType type);

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
  <U, W extends UnitWrapper> UnitBroker registerUnit(W unitWrapper, Class<U> unitClass, UnitMethodDescription... methods);

  <R> Action0<R> objectAssistantAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<R> targetObjectHandleType
  );

  <R, Q> Action1<R, Q> objectAssistantAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q> contractQualifierType,
      Type<R> targetObjectHandleType
  );

  <R, Q1, Q2> Action2<R, Q1, Q2> objectAssistantAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<R> targetObjectHandleType
  );

  <R, Q1, Q2, Q3> Action3<R, Q1, Q2, Q3> objectAssistantAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<R> targetObjectHandleType
  );

  <R, Q1, Q2, Q3, Q4> Action4<R, Q1, Q2, Q3, Q4> objectAssistantAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<Q4> contractQualifierType4,
      Type<R> targetObjectHandleType
  );

  <R, Q1, Q2, Q3, Q4, Q5> Action5<R, Q1, Q2, Q3, Q4, Q5> objectAssistantAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<Q4> contractQualifierType4,
      Type<Q5> contractQualifierType5,
      Type<R> targetObjectHandleType
  );

  <R, Q1, Q2, Q3, Q4, Q5, Q6> Action6<R, Q1, Q2, Q3, Q4, Q5, Q6> objectAssistantAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<Q4> contractQualifierType4,
      Type<Q5> contractQualifierType5,
      Type<Q6> contractQualifierType6,
      Type<R> targetObjectHandleType
  );

  <R, Q1, Q2, Q3, Q4, Q5, Q6, Q7> Action7<R, Q1, Q2, Q3, Q4, Q5, Q6, Q7> objectAssistantAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<Q4> contractQualifierType4,
      Type<Q5> contractQualifierType5,
      Type<Q6> contractQualifierType6,
      Type<Q7> contractQualifierType7,
      Type<R> targetObjectHandleType
  );

  <R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8> Action8<R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8> objectAssistantAction(
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
      Type<R> targetObjectHandleType
  );

  <R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9> Action9<R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9> objectAssistantAction(
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
      Type<R> targetObjectHandleType
  );

  <R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10> Action10<R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10> objectAssistantAction(
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
      Type<R> targetObjectHandleType
  );
}
