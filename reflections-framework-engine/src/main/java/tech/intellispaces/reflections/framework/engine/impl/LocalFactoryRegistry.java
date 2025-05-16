package tech.intellispaces.reflections.framework.engine.impl;

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
import tech.intellispaces.reflections.framework.engine.FactoryRegistry;
import tech.intellispaces.reflections.framework.factory.FactoryWrapper;
import tech.intellispaces.reflections.framework.factory.FactoryMethod;
import tech.intellispaces.reflections.framework.exception.ConfigurationExceptions;
import tech.intellispaces.reflections.framework.naming.NameConventionFunctions;
import tech.intellispaces.reflections.framework.factory.FactoryFunctions;

class LocalFactoryRegistry implements FactoryRegistry {
  private boolean isLoaded;
  private Map<Class<?>, List<FactoryMethod>> domainToDescriptions = Map.of();

  @Override
  @SuppressWarnings("unchecked")
  public <R> Action0<R> getFactoryAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<R> targetReflectionType
  ) {
    loadObjectFactories();
    return (Action0<R>) makeAction(targetDomainClass, contractType, List.of());
  }

  @Override
  @SuppressWarnings("unchecked")
  public <R, Q> Action1<R, Q> getFactoryAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q> contractQualifierType,
      Type<R> targetReflectionType
  ) {
    loadObjectFactories();
    return (Action1<R, Q>) makeAction(targetDomainClass, contractType, List.of(contractQualifierType));
  }

  @Override
  @SuppressWarnings("unchecked")
  public <R, Q1, Q2> Action2<R, Q1, Q2> getFactoryAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<R> targetReflectionType
  ) {
    loadObjectFactories();
    return (Action2<R, Q1, Q2>) makeAction(
        targetDomainClass, contractType, List.of(contractQualifierType1, contractQualifierType2
    ));
  }

  @Override
  @SuppressWarnings("unchecked")
  public <R, Q1, Q2, Q3> Action3<R, Q1, Q2, Q3> getFactoryAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<R> targetReflectionType
  ) {
    loadObjectFactories();
    return (Action3<R, Q1, Q2, Q3>) makeAction(
        targetDomainClass, contractType, List.of(contractQualifierType1, contractQualifierType2, contractQualifierType3
    ));
  }

  @Override
  @SuppressWarnings("unchecked")
  public <R, Q1, Q2, Q3, Q4> Action4<R, Q1, Q2, Q3, Q4> getFactoryAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<Q4> contractQualifierType4,
      Type<R> targetReflectionType
  ) {
    loadObjectFactories();
    return (Action4<R, Q1, Q2, Q3, Q4>) makeAction(
        targetDomainClass,
        contractType, List.of(
            contractQualifierType1,
            contractQualifierType2,
            contractQualifierType3,
            contractQualifierType4
    ));
  }

  @Override
  @SuppressWarnings("unchecked")
  public <R, Q1, Q2, Q3, Q4, Q5> Action5<R, Q1, Q2, Q3, Q4, Q5> getFactoryAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<Q4> contractQualifierType4,
      Type<Q5> contractQualifierType5,
      Type<R> targetReflectionType
  ) {
    loadObjectFactories();
    return (Action5<R, Q1, Q2, Q3, Q4, Q5>) makeAction(
        targetDomainClass,
        contractType, List.of(
            contractQualifierType1,
            contractQualifierType2,
            contractQualifierType3,
            contractQualifierType4,
            contractQualifierType5
    ));
  }

  @Override
  @SuppressWarnings("unchecked")
  public <R, Q1, Q2, Q3, Q4, Q5, Q6> Action6<R, Q1, Q2, Q3, Q4, Q5, Q6> getFactoryAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<Q4> contractQualifierType4,
      Type<Q5> contractQualifierType5,
      Type<Q6> contractQualifierType6,
      Type<R> targetReflectionType
  ) {
    loadObjectFactories();
    return (Action6<R, Q1, Q2, Q3, Q4, Q5, Q6>) makeAction(
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

  @Override
  @SuppressWarnings("unchecked")
  public <R, Q1, Q2, Q3, Q4, Q5, Q6, Q7> Action7<R, Q1, Q2, Q3, Q4, Q5, Q6, Q7> getFactoryAction(
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
  ) {
    loadObjectFactories();
    return (Action7<R, Q1, Q2, Q3, Q4, Q5, Q6, Q7>) makeAction(
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

  @Override
  @SuppressWarnings("unchecked")
  public <R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8> Action8<R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8> getFactoryAction(
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
  ) {
    loadObjectFactories();
    return (Action8<R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8>) makeAction(
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

  @Override
  @SuppressWarnings("unchecked")
  public <R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9> Action9<R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9> getFactoryAction(
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
  ) {
    loadObjectFactories();
    return (Action9<R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9>) makeAction(
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

  @Override
  @SuppressWarnings("unchecked")
  public <R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10> Action10<R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10> getFactoryAction(
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
  ) {
    loadObjectFactories();
    return (Action10<R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10>) makeAction(
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

    List<FactoryMethod> descriptions = new ArrayList<>();
    try {
      Enumeration<URL> enumeration = LocalFactoryRegistry.class.getClassLoader().getResources(
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
          var wrapper = (FactoryWrapper) Objects.get(factoryClass.get());
          descriptions.addAll(wrapper.methods());
        }
      }
    } catch (IOException e) {
      throw UnexpectedExceptions.withCauseAndMessage(e, "Unable to load object factories");
    }

    domainToDescriptions = descriptions.stream()
        .collect(Collectors.groupingBy(FactoryMethod::returnedDomainClass));
    isLoaded = true;
  }

  boolean isMatchContractQualifiers(
      FactoryMethod description, List<Type<?>> requiredQualifierTypes
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
    List<FactoryMethod> descriptions = domainToDescriptions.get(targetDomainClass);
    if (descriptions == null) {
      throw ConfigurationExceptions.withMessage("No factory to domain {0} were found",
          targetDomainClass.getCanonicalName());
    }
    for (FactoryMethod description : descriptions) {
      if (
          FactoryFunctions.getContractType(description.name()).equals(contractType) &&
              isMatchContractQualifiers(description, contractQualifierTypes)
      ) {
        return makeAction(description);
      }
    }
    throw ConfigurationExceptions.withMessage("No factory to domain {0} and contract type {1} were found",
        targetDomainClass.getCanonicalName(), contractType);
  }

  Action makeAction(FactoryMethod description) {
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
  private Action makeAction0(FactoryMethod description) {
    var originAction = (Action1<Object, Object>) description.action();
    return originAction.convertToAction0(description.factoryInstance());
  }

  @SuppressWarnings("unchecked")
  private Action makeAction1(FactoryMethod description) {
    var originAction = (Action2<Object, Object, Object>) description.action();
    return originAction.convertToAction1(description.factoryInstance());
  }

  @SuppressWarnings("unchecked")
  private Action makeAction2(FactoryMethod description) {
    var originAction = (Action3<Object, Object, Object, Object>) description.action();
    return originAction.convertToAction2(description.factoryInstance());
  }

  @SuppressWarnings("unchecked")
  private Action makeAction3(FactoryMethod description) {
    var originAction = (Action4<Object, Object, Object, Object, Object>) description.action();
    return originAction.convertToAction3(description.factoryInstance());
  }

  @SuppressWarnings("unchecked")
  private Action makeAction4(FactoryMethod description) {
    var originAction = (Action5<Object, Object, Object, Object, Object, Object>) description.action();
    return originAction.convertToAction4(description.factoryInstance());
  }

  @SuppressWarnings("unchecked")
  private Action makeAction5(FactoryMethod description) {
    var originAction = (Action6<Object, Object, Object, Object, Object, Object, Object>) description.action();
    return originAction.convertToAction5(description.factoryInstance());
  }

  @SuppressWarnings("unchecked")
  private Action makeAction6(FactoryMethod description) {
    var originAction = (Action7<Object, Object, Object, Object, Object, Object, Object, Object>) description.action();
    return originAction.convertToAction6(description.factoryInstance());
  }

  @SuppressWarnings("unchecked")
  private Action makeAction7(FactoryMethod description) {
    var originAction = (Action8<Object, Object, Object, Object, Object, Object, Object, Object, Object>) description.action();
    return originAction.convertToAction7(description.factoryInstance());
  }

  @SuppressWarnings("unchecked")
  private Action makeAction8(FactoryMethod description) {
    var originAction = (Action9<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object>) description.action();
    return originAction.convertToAction8(description.factoryInstance());
  }

  @SuppressWarnings("unchecked")
  private Action makeAction9(FactoryMethod description) {
    var originAction = (Action10<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object>) description.action();
    return originAction.convertToAction9(description.factoryInstance());
  }
}
