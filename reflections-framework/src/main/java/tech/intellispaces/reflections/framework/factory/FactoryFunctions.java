package tech.intellispaces.reflections.framework.factory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

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
import tech.intellispaces.actions.functional.FunctionActions;
import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.properties.PropertiesSet;
import tech.intellispaces.commons.text.StringFunctions;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.core.Domain;
import tech.intellispaces.core.Reflection;
import tech.intellispaces.javareflection.method.MethodStatement;
import tech.intellispaces.reflections.framework.annotation.Contract;
import tech.intellispaces.reflections.framework.reflection.NativeForeignReflection;
import tech.intellispaces.reflections.framework.reflection.NativeReflection;
import tech.intellispaces.reflections.framework.space.domain.DomainFunctions;

public interface FactoryFunctions {

  static String getContractType(MethodStatement method) {
    Optional<Contract> contract = method.selectAnnotation(Contract.class);
    if (contract.isPresent()) {
      return DomainFunctions.getDomainName(contract.get().value());
    }
    return getContractType(method.name());
  }

  private static String getContractType(String methodName) {
    if (methodName.equals("create") || (methodName.startsWith("create") && StringFunctions.isUppercaseChar(methodName, 6))) {
      return "create";
    } else if (methodName.equals("reflectionOf") || (methodName.startsWith("reflectionOf") && StringFunctions.isUppercaseChar(methodName, 12))) {
      return "reflection";
    }
    return methodName;
  }

  static  <R extends Reflection> Action1<R, PropertiesSet> createFactoryAction(
      Domain targetDomain, FactoryMethod factoryMethod
  ) {
    int numQualifiers = factoryMethod.contractQualifierTypes().size();
    return switch (numQualifiers) {
      case 1 -> createFactoryAction1(targetDomain, factoryMethod);
      case 2 -> createFactoryAction2(targetDomain, factoryMethod);
      default -> throw NotImplementedExceptions.withCode("OSIoFg");
    };
  }

  @SuppressWarnings("unchecked")
  private static <R extends Reflection> Action1<R, PropertiesSet> createFactoryAction1(
      Domain targetDomain, FactoryMethod factoryMethod
  ) {
    var rootAction = (Action1<R, Object>) makeAction1(factoryMethod);
    Function<PropertiesSet, R> function = (PropertiesSet props) -> {
      List<Object> qualifierValues = getQualifierValues(props, factoryMethod);
      return castToTargetDomain(rootAction.execute(qualifierValues.get(0)), targetDomain);
    };
    return FunctionActions.ofFunction(function);
  }

  @SuppressWarnings("unchecked")
  private static <R extends Reflection> Action1<R, PropertiesSet> createFactoryAction2(
      Domain targetDomain, FactoryMethod factoryMethod
  ) {
    var rootAction = (Action2<R, Object, Object>) makeAction2(factoryMethod);
    Function<PropertiesSet, R> function = (PropertiesSet props) -> {
      List<Object> qualifierValues = getQualifierValues(props, factoryMethod);
      return castToTargetDomain(rootAction.execute(qualifierValues.get(0), qualifierValues.get(1)), targetDomain);
    };
    return FunctionActions.ofFunction(function);
  }

  @SuppressWarnings("unchecked")
  private static <R extends Reflection> R castToTargetDomain(
      R reflection, Domain targetDomain
  ) {
    if (targetDomain.foreignDomainName() == null) {
      return reflection;
    }
    return (R) new NativeForeignReflection<>((NativeReflection<?>) reflection, targetDomain);
  }

  private static List<Object> getQualifierValues(PropertiesSet props, FactoryMethod factoryMethod) {
    var qualifierValues = new ArrayList<>(factoryMethod.contractQualifierNames().size());
    Iterator<String> qualifierNames = factoryMethod.contractQualifierNames().iterator();
    Iterator<Type<?>> qualifierTypes = factoryMethod.contractQualifierTypes().iterator();
    while (qualifierNames.hasNext() && qualifierTypes.hasNext()) {
      String qualifierName = qualifierNames.next();
      Type<?> qualifierType = qualifierTypes.next();
      qualifierValues.add(getQualifierValue(props, qualifierName, qualifierType));
    }
    return qualifierValues;
  }

  private static Object getQualifierValue(
      PropertiesSet props, String qualifierName, Type<?> qualifierType
  ) {
    if (qualifierType.asClassType().baseClass() == String.class) {
      return props.traverse(qualifierName);
    }
    throw NotImplementedExceptions.withCode("7Ro3NQ");
  }

  static Action makeAction(FactoryMethod factoryMethod) {
    return switch (factoryMethod.contractQualifierTypes().size()) {
      case 0 -> makeAction0(factoryMethod);
      case 1 -> makeAction1(factoryMethod);
      case 2 -> makeAction2(factoryMethod);
      case 3 -> makeAction3(factoryMethod);
      case 4 -> makeAction4(factoryMethod);
      case 5 -> makeAction5(factoryMethod);
      case 6 -> makeAction6(factoryMethod);
      case 7 -> makeAction7(factoryMethod);
      case 8 -> makeAction8(factoryMethod);
      case 9 -> makeAction9(factoryMethod);
      default -> throw NotImplementedExceptions.withCode("h3he6A");
    };
  }

  @SuppressWarnings("unchecked")
  static Action0<?> makeAction0(FactoryMethod factoryMethod) {
    var originAction = (Action1<Object, Object>) factoryMethod.action();
    return originAction.convertToAction0(factoryMethod.factoryInstance());
  }

  @SuppressWarnings("unchecked")
  static Action1<?, ?> makeAction1(FactoryMethod factoryMethod) {
    var originAction = (Action2<Object, Object, Object>) factoryMethod.action();
    return originAction.convertToAction1(factoryMethod.factoryInstance());
  }

  @SuppressWarnings("unchecked")
  static Action2<?, ?, ?> makeAction2(FactoryMethod factoryMethod) {
    var originAction = (Action3<Object, Object, Object, Object>) factoryMethod.action();
    return originAction.convertToAction2(factoryMethod.factoryInstance());
  }

  @SuppressWarnings("unchecked")
  static Action3<?, ?, ?, ?> makeAction3(FactoryMethod factoryMethod) {
    var originAction = (Action4<Object, Object, Object, Object, Object>) factoryMethod.action();
    return originAction.convertToAction3(factoryMethod.factoryInstance());
  }

  @SuppressWarnings("unchecked")
  static Action4<?, ?, ?, ?, ?> makeAction4(FactoryMethod factoryMethod) {
    var originAction = (Action5<Object, Object, Object, Object, Object, Object>) factoryMethod.action();
    return originAction.convertToAction4(factoryMethod.factoryInstance());
  }

  @SuppressWarnings("unchecked")
  static Action5<?, ?, ?, ?, ?, ?> makeAction5(FactoryMethod factoryMethod) {
    var originAction = (Action6<Object, Object, Object, Object, Object, Object, Object>) factoryMethod.action();
    return originAction.convertToAction5(factoryMethod.factoryInstance());
  }

  @SuppressWarnings("unchecked")
  static Action6<?, ?, ?, ?, ?, ?, ?> makeAction6(FactoryMethod factoryMethod) {
    var originAction = (Action7<Object, Object, Object, Object, Object, Object, Object, Object>) factoryMethod.action();
    return originAction.convertToAction6(factoryMethod.factoryInstance());
  }

  @SuppressWarnings("unchecked")
  static Action7<?, ?, ?, ?, ?, ?, ?, ?> makeAction7(FactoryMethod factoryMethod) {
    var originAction = (Action8<Object, Object, Object, Object, Object, Object, Object, Object, Object>) factoryMethod.action();
    return originAction.convertToAction7(factoryMethod.factoryInstance());
  }

  @SuppressWarnings("unchecked")
  static Action8<?, ?, ?, ?, ?, ?, ?, ?, ?> makeAction8(FactoryMethod factoryMethod) {
    var originAction = (Action9<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object>) factoryMethod.action();
    return originAction.convertToAction8(factoryMethod.factoryInstance());
  }

  @SuppressWarnings("unchecked")
  static Action9<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> makeAction9(FactoryMethod factoryMethod) {
    var originAction = (Action10<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object>) factoryMethod.action();
    return originAction.convertToAction9(factoryMethod.factoryInstance());
  }
}
