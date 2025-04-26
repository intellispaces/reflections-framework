package tech.intellispaces.jaquarius.engine;

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
import tech.intellispaces.jaquarius.engine.description.ObjectHandleMethodDescription;
import tech.intellispaces.jaquarius.engine.description.ObjectHandleTypeDescription;
import tech.intellispaces.jaquarius.engine.description.UnitMethodDescription;
import tech.intellispaces.jaquarius.system.Module;
import tech.intellispaces.jaquarius.system.UnitWrapper;

/**
 * Jaquarius engine API.
 */
public interface JaquariusEngine {

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
   * Registers object handle type.
   *
   * @param wrapperClass the object handle wrapper class.
   * @param objectHandleClass the object handle class.
   * @param methods object handle wrapper method descriptions.
   * @return the registered object handle type.
   *
   * @param <H> the object handle type.
   * @param <W> the object handle wrapper type.
   */
  <H, W extends H> ObjectHandleTypeDescription registerObjectHandleType(
      Class<W> wrapperClass, Class<H> objectHandleClass, ObjectHandleMethodDescription... methods
  );

  /**
   * Registers object handle.
   *
   * @param objectHandleWrapper the object handle wrapper.
   * @param type the object handle type.
   * @return the object handle broker.
   *
   * @param <W> the object handle wrapper type.
   */
  <W> ObjectHandleBroker registerObjectHandle(W objectHandleWrapper, ObjectHandleTypeDescription type);

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

  <H> Action0<H> objectAssistantAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<H> targetObjectHandleType
  );

  <H, Q> Action1<H, Q> objectAssistantAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q> contractQualifierType,
      Type<H> targetObjectHandleType
  );

  <H, Q1, Q2> Action2<H, Q1, Q2> objectAssistantAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<H> targetObjectHandleType
  );

  <H, Q1, Q2, Q3> Action3<H, Q1, Q2, Q3> objectAssistantAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<H> targetObjectHandleType
  );

  <H, Q1, Q2, Q3, Q4> Action4<H, Q1, Q2, Q3, Q4> objectAssistantAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<Q4> contractQualifierType4,
      Type<H> targetObjectHandleType
  );

  <H, Q1, Q2, Q3, Q4, Q5> Action5<H, Q1, Q2, Q3, Q4, Q5> objectAssistantAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<Q4> contractQualifierType4,
      Type<Q5> contractQualifierType5,
      Type<H> targetObjectHandleType
  );

  <H, Q1, Q2, Q3, Q4, Q5, Q6> Action6<H, Q1, Q2, Q3, Q4, Q5, Q6> objectAssistantAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<Q4> contractQualifierType4,
      Type<Q5> contractQualifierType5,
      Type<Q6> contractQualifierType6,
      Type<H> targetObjectHandleType
  );

  <H, Q1, Q2, Q3, Q4, Q5, Q6, Q7> Action7<H, Q1, Q2, Q3, Q4, Q5, Q6, Q7> objectAssistantAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<Q4> contractQualifierType4,
      Type<Q5> contractQualifierType5,
      Type<Q6> contractQualifierType6,
      Type<Q7> contractQualifierType7,
      Type<H> targetObjectHandleType
  );

  <H, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8> Action8<H, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8> objectAssistantAction(
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
      Type<H> targetObjectHandleType
  );

  <H, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9> Action9<H, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9> objectAssistantAction(
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
      Type<H> targetObjectHandleType
  );

  <H, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10> Action10<H, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10> objectAssistantAction(
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
      Type<H> targetObjectHandleType
  );
}
