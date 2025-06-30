package tech.intellispaces.reflections.framework.system;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.object.Objects;
import tech.intellispaces.commons.properties.PropertiesSet;
import tech.intellispaces.commons.resource.ResourceFunctions;
import tech.intellispaces.commons.type.Classes;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.core.Domain;
import tech.intellispaces.core.Reflection;
import tech.intellispaces.core.ReflectionFactory;
import tech.intellispaces.core.Rid;
import tech.intellispaces.reflections.framework.exception.ConfigurationExceptions;
import tech.intellispaces.reflections.framework.factory.FactoryFunctions;
import tech.intellispaces.reflections.framework.factory.FactoryMethod;
import tech.intellispaces.reflections.framework.factory.FactoryWrapper;
import tech.intellispaces.reflections.framework.naming.NameConventionFunctions;

public class LocalFactoryRegistry implements FactoryRegistry {
  private boolean isLoaded;
  private Map<Rid, List<FactoryMethod>> domainRidToFactoryMethods = Map.of();
  private Map<String, List<FactoryMethod>> domainNameToFactoryMethods = Map.of();
  private Map<Class<?>, List<FactoryMethod>> domainClassToFactoryMethods = Map.of();

  @Override
  public List<ReflectionFactory> findFactories(Domain domain) {
    loadFactories();
    Set<FactoryMethod> factories = new HashSet<>();
    findFactoryMethods(domain, factories);

    Domain borrowedDomain = domain.borrowedDomain();
    if (borrowedDomain != null) {
      findFactoryMethods(borrowedDomain, factories);
    }

    return factories.stream()
        .map(it -> (ReflectionFactory) it)
        .toList();
  }

  @Override
  public <R extends Reflection> Action1<R, PropertiesSet> factoryAction(
      Domain targetDomain, String contractType
  ) {
    Collection<FactoryMethod> factoryMethods = findFactoryMethods(targetDomain);
    for (FactoryMethod factoryMethod : factoryMethods) {
      if (factoryMethod.contractType().equals(contractType)) {
        return FactoryFunctions.createFactoryAction(targetDomain, factoryMethod);
      }
    }
    throw ConfigurationExceptions.withMessage("No factory for contract type {0} were found", contractType);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <R> Action0<R> getFactoryAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<R> targetReflectionType
  ) {
    loadFactories();
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
    loadFactories();
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
    loadFactories();
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
    loadFactories();
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
    loadFactories();
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
    loadFactories();
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
    loadFactories();
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
    loadFactories();
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
    loadFactories();
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
    loadFactories();
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
    loadFactories();
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

  void loadFactories() {
    if (isLoaded) {
      return;
    }

    List<FactoryMethod> factoryMethods = new ArrayList<>();
    try {
      Enumeration<URL> enumeration = LocalFactoryRegistry.class.getClassLoader().getResources(
          NameConventionFunctions.getFactoriesResourceName()
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
            throw UnexpectedExceptions.withMessage("Unable to load factory class {0}", factoryClassName);
          }
          var wrapper = (FactoryWrapper) Objects.get(factoryClass.get());
          factoryMethods.addAll(wrapper.methods());
        }
      }
    } catch (IOException e) {
      throw UnexpectedExceptions.withCauseAndMessage(e, "Unable to load factories");
    }

    domainRidToFactoryMethods = factoryMethods.stream()
        .filter(factoryMethod -> factoryMethod.outputDomain().rid() != null)
        .collect(Collectors.groupingBy(factoryMethod -> factoryMethod.outputDomain().rid()));
    domainNameToFactoryMethods = factoryMethods.stream()
        .filter(factoryMethod -> factoryMethod.outputDomain().reflectionName() != null)
        .collect(Collectors.groupingBy(factoryMethod -> factoryMethod.outputDomain().reflectionName()));
    domainClassToFactoryMethods = factoryMethods.stream()
        .filter(factoryMethod -> factoryMethod.outputDomain().domainClass() != null)
        .collect(Collectors.groupingBy(factoryMethod -> factoryMethod.outputDomain().domainClass()));
    isLoaded = true;
  }

  boolean isMatchContractQualifiers(
      FactoryMethod description, List<Type<?>> requiredQualifierTypes
  ) {
    List<Type<?>> factoryQualifierTypes = description.contractQualifierTypes();
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
    List<FactoryMethod> factoryMethods = domainClassToFactoryMethods.get(targetDomainClass);
    if (factoryMethods == null) {
      throw ConfigurationExceptions.withMessage("No factory for domain {0} were found",
          targetDomainClass.getCanonicalName());
    }
    for (FactoryMethod factoryMethod : factoryMethods) {
      if (
          factoryMethod.contractType().equals(contractType) &&
              isMatchContractQualifiers(factoryMethod, contractQualifierTypes)
      ) {
        return FactoryFunctions.makeAction(factoryMethod);
      }
    }
    throw ConfigurationExceptions.withMessage("No factory for domain {0} and contract type {1} were found",
        targetDomainClass.getCanonicalName(), contractType);
  }

  Collection<FactoryMethod> findFactoryMethods(Domain domain) {
    Set<FactoryMethod> factoryMethods = new HashSet<>();
    findFactoryMethods(domain, factoryMethods);

    Domain borrowedDomain = domain.borrowedDomain();
    if (borrowedDomain != null) {
      findFactoryMethods(borrowedDomain, factoryMethods);
    }
    return factoryMethods;
  }

  private void findFactoryMethods(Domain domain, Collection<FactoryMethod> factoryMethods) {
    if (domain.rid() != null) {
      factoryMethods.addAll(domainRidToFactoryMethods.getOrDefault(domain.rid(), List.of()));
    }
    if (domain.reflectionName() != null) {
      factoryMethods.addAll(domainNameToFactoryMethods.getOrDefault(domain.reflectionName(), List.of()));
    }
    if (domain.domainClass() != null) {
      factoryMethods.addAll(domainClassToFactoryMethods.getOrDefault(domain.domainClass(), List.of()));
    }
  }
}
