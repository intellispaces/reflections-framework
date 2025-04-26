package tech.intellispaces.jaquarius.engine.impl;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import tech.intellispaces.actions.Action;
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
import tech.intellispaces.commons.collection.CollectionFunctions;
import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.object.Objects;
import tech.intellispaces.commons.resource.ResourceFunctions;
import tech.intellispaces.commons.type.Classes;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.jaquarius.engine.ObjectFactoryWrapper;
import tech.intellispaces.jaquarius.engine.description.ObjectFactoryMethodDescription;
import tech.intellispaces.jaquarius.exception.ConfigurationExceptions;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;
import tech.intellispaces.jaquarius.object.factory.ObjectFactoryFunctions;

class ObjectFactoryRegistry {
  private boolean isLoaded;
  private Map<Class<?>, List<ObjectFactoryMethodDescription>> domainToDescriptions = Map.of();

  @SuppressWarnings("unchecked")
  public <H> Action0<H> objectAssistantAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<H> targetObjectHandleType
  ) {
    loadObjectFactories();
    return (Action0<H>) makeAction(targetDomainClass, contractType, List.of());
  }

  @SuppressWarnings("unchecked")
  public <H, Q> Action1<H, Q> objectAssistantAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q> contractQualifierType,
      Type<H> targetObjectHandleType
  ) {
    loadObjectFactories();
    return (Action1<H, Q>) makeAction(targetDomainClass, contractType, List.of(contractQualifierType));
  }

  @SuppressWarnings("unchecked")
  public <H, Q1, Q2> Action2<H, Q1, Q2> objectAssistantAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<H> targetObjectHandleType
  ) {
    loadObjectFactories();
    return (Action2<H, Q1, Q2>) makeAction(
        targetDomainClass, contractType, List.of(contractQualifierType1, contractQualifierType2
    ));
  }

  @SuppressWarnings("unchecked")
  public <H, Q1, Q2, Q3> Action3<H, Q1, Q2, Q3> objectAssistantAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<H> targetObjectHandleType
  ) {
    loadObjectFactories();
    return (Action3<H, Q1, Q2, Q3>) makeAction(
        targetDomainClass, contractType, List.of(contractQualifierType1, contractQualifierType2, contractQualifierType3
    ));
  }

  @SuppressWarnings("unchecked")
  public <H, Q1, Q2, Q3, Q4> Action4<H, Q1, Q2, Q3, Q4> objectAssistantAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<Q4> contractQualifierType4,
      Type<H> targetObjectHandleType
  ) {
    loadObjectFactories();
    return (Action4<H, Q1, Q2, Q3, Q4>) makeAction(
        targetDomainClass,
        contractType, List.of(
            contractQualifierType1,
            contractQualifierType2,
            contractQualifierType3,
            contractQualifierType4
    ));
  }

  @SuppressWarnings("unchecked")
  public <H, Q1, Q2, Q3, Q4, Q5> Action5<H, Q1, Q2, Q3, Q4, Q5> objectAssistantAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<Q4> contractQualifierType4,
      Type<Q5> contractQualifierType5,
      Type<H> targetObjectHandleType
  ) {
    loadObjectFactories();
    return (Action5<H, Q1, Q2, Q3, Q4, Q5>) makeAction(
        targetDomainClass,
        contractType, List.of(
            contractQualifierType1,
            contractQualifierType2,
            contractQualifierType3,
            contractQualifierType4,
            contractQualifierType5
    ));
  }

  @SuppressWarnings("unchecked")
  public <H, Q1, Q2, Q3, Q4, Q5, Q6> Action6<H, Q1, Q2, Q3, Q4, Q5, Q6> objectAssistantAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<Q4> contractQualifierType4,
      Type<Q5> contractQualifierType5,
      Type<Q6> contractQualifierType6,
      Type<H> targetObjectHandleType
  ) {
    loadObjectFactories();
    return (Action6<H, Q1, Q2, Q3, Q4, Q5, Q6>) makeAction(
        targetDomainClass,
        contractType, List.of(
            contractQualifierType1,
            contractQualifierType2,
            contractQualifierType3,
            contractQualifierType4,
            contractQualifierType5,
            contractQualifierType6
    ));
  }

  @SuppressWarnings("unchecked")
  public <H, Q1, Q2, Q3, Q4, Q5, Q6, Q7> Action7<H, Q1, Q2, Q3, Q4, Q5, Q6, Q7> objectAssistantAction(
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
  ) {
    loadObjectFactories();
    return (Action7<H, Q1, Q2, Q3, Q4, Q5, Q6, Q7>) makeAction(
        targetDomainClass,
        contractType, List.of(
            contractQualifierType1,
            contractQualifierType2,
            contractQualifierType3,
            contractQualifierType4,
            contractQualifierType5,
            contractQualifierType6,
            contractQualifierType7
    ));
  }

  @SuppressWarnings("unchecked")
  public <H, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8> Action8<H, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8> objectAssistantAction(
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
  ) {
    loadObjectFactories();
    return (Action8<H, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8>) makeAction(
        targetDomainClass,
        contractType, List.of(
            contractQualifierType1,
            contractQualifierType2,
            contractQualifierType3,
            contractQualifierType4,
            contractQualifierType5,
            contractQualifierType6,
            contractQualifierType7,
            contractQualifierType8
    ));
  }

  @SuppressWarnings("unchecked")
  public <H, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9> Action9<H, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9> objectAssistantAction(
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
  ) {
    loadObjectFactories();
    return (Action9<H, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9>) makeAction(
        targetDomainClass,
        contractType, List.of(
            contractQualifierType1,
            contractQualifierType2,
            contractQualifierType3,
            contractQualifierType4,
            contractQualifierType5,
            contractQualifierType6,
            contractQualifierType7,
            contractQualifierType8,
            contractQualifierType9
    ));
  }

  @SuppressWarnings("unchecked")
  public <H, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10> Action10<H, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10> objectAssistantAction(
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
  ) {
    loadObjectFactories();
    return (Action10<H, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10>) makeAction(
        targetDomainClass,
        contractType, List.of(
            contractQualifierType1,
            contractQualifierType2,
            contractQualifierType3,
            contractQualifierType4,
            contractQualifierType5,
            contractQualifierType6,
            contractQualifierType7,
            contractQualifierType8,
            contractQualifierType9,
            contractQualifierType10
    ));
  }

  void loadObjectFactories() {
    if (isLoaded) {
      return;
    }

    List<ObjectFactoryMethodDescription> descriptions = new ArrayList<>();
    try {
      Enumeration<URL> enumeration = ObjectFactoryRegistry.class.getClassLoader().getResources(
          NameConventionFunctions.getObjectFactoriesResourceName()
      );
      List<URL> urls = CollectionFunctions.toList(enumeration);
      for (URL url : urls) {
        String content = ResourceFunctions.readResourceAsString(url);
        for (String factoryClassName : content.split("\n")) {
          factoryClassName = factoryClassName.trim();
          if (factoryClassName.isEmpty()) {
            continue;
          }
          Optional<Class<?>> factoryClass = Classes.get(factoryClassName);
          if (factoryClass.isEmpty()) {
            throw UnexpectedExceptions.withMessage("Unable to load object factory class {0}", factoryClassName);
          }
          var wrapper = (ObjectFactoryWrapper) Objects.get(factoryClass.get());
          descriptions.addAll(wrapper.methods());
        }
      }
    } catch (IOException e) {
      throw UnexpectedExceptions.withCauseAndMessage(e, "Unable to load object factories");
    }

    domainToDescriptions = descriptions.stream()
        .collect(Collectors.groupingBy(ObjectFactoryMethodDescription::returnedDomainClass));
    isLoaded = true;
  }

  boolean isMatchContractQualifiers(
      ObjectFactoryMethodDescription description, List<Type<?>> requiredQualifierTypes
  ) {
    List<Type<?>> factoryQualifierTypes = description.paramTypes();
    if (factoryQualifierTypes.size() != requiredQualifierTypes.size()) {
      return false;
    }
    for (int index = 0; index < factoryQualifierTypes.size(); index++) {
      Type<?> factoryQualifierType = factoryQualifierTypes.get(index);
      Type<?> requiredQualifierType = requiredQualifierTypes.get(index);
      if (!factoryQualifierType.equals(requiredQualifierType)) {
        return false;
      }
    }
    return true;
  }

  Action makeAction(
      Class<?> targetDomainClass,
      String contractType,
      List<Type<?>> contractQualifierTypes
  ) {
    List<ObjectFactoryMethodDescription> descriptions = domainToDescriptions.get(targetDomainClass);
    if (descriptions == null) {
      throw ConfigurationExceptions.withMessage("No factory to domain {0} were found",
          targetDomainClass.getCanonicalName());
    }
    for (ObjectFactoryMethodDescription description : descriptions) {
      if (
          ObjectFactoryFunctions.getContractType(description.name()).equals(contractType) &&
              isMatchContractQualifiers(description, contractQualifierTypes)
      ) {
        return makeAction(description);
      }
    }
    throw ConfigurationExceptions.withMessage("No factory to domain {0} and contract type {1} were found",
        targetDomainClass.getCanonicalName(), contractType);
  }

  Action makeAction(ObjectFactoryMethodDescription description) {
    return switch (description.paramTypes().size()) {
      case 0 -> makeAction0(description);
      case 1 -> makeAction1(description);
      case 2 -> makeAction2(description);
      case 3 -> makeAction3(description);
      case 4 -> makeAction4(description);
      case 5 -> makeAction5(description);
      case 6 -> makeAction6(description);
      case 7 -> makeAction7(description);
      case 8 -> makeAction8(description);
      case 9 -> makeAction9(description);
      default -> throw NotImplementedExceptions.withCode("h3he6A");
    };
  }

  @SuppressWarnings("unchecked")
  private Action makeAction0(ObjectFactoryMethodDescription description) {
    var originAction = (Action1<Object, Object>) description.action();
    return originAction.convertToAction0(description.objectFactory());
  }

  @SuppressWarnings("unchecked")
  private Action makeAction1(ObjectFactoryMethodDescription description) {
    var originAction = (Action2<Object, Object, Object>) description.action();
    return originAction.convertToAction1(description.objectFactory());
  }

  @SuppressWarnings("unchecked")
  private Action makeAction2(ObjectFactoryMethodDescription description) {
    var originAction = (Action3<Object, Object, Object, Object>) description.action();
    return originAction.convertToAction2(description.objectFactory());
  }

  @SuppressWarnings("unchecked")
  private Action makeAction3(ObjectFactoryMethodDescription description) {
    var originAction = (Action4<Object, Object, Object, Object, Object>) description.action();
    return originAction.convertToAction3(description.objectFactory());
  }

  @SuppressWarnings("unchecked")
  private Action makeAction4(ObjectFactoryMethodDescription description) {
    var originAction = (Action5<Object, Object, Object, Object, Object, Object>) description.action();
    return originAction.convertToAction4(description.objectFactory());
  }

  @SuppressWarnings("unchecked")
  private Action makeAction5(ObjectFactoryMethodDescription description) {
    var originAction = (Action6<Object, Object, Object, Object, Object, Object, Object>) description.action();
    return originAction.convertToAction5(description.objectFactory());
  }

  @SuppressWarnings("unchecked")
  private Action makeAction6(ObjectFactoryMethodDescription description) {
    var originAction = (Action7<Object, Object, Object, Object, Object, Object, Object, Object>) description.action();
    return originAction.convertToAction6(description.objectFactory());
  }

  @SuppressWarnings("unchecked")
  private Action makeAction7(ObjectFactoryMethodDescription description) {
    var originAction = (Action8<Object, Object, Object, Object, Object, Object, Object, Object, Object>) description.action();
    return originAction.convertToAction7(description.objectFactory());
  }

  @SuppressWarnings("unchecked")
  private Action makeAction8(ObjectFactoryMethodDescription description) {
    var originAction = (Action9<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object>) description.action();
    return originAction.convertToAction8(description.objectFactory());
  }

  @SuppressWarnings("unchecked")
  private Action makeAction9(ObjectFactoryMethodDescription description) {
    var originAction = (Action10<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object>) description.action();
    return originAction.convertToAction9(description.objectFactory());
  }
}
