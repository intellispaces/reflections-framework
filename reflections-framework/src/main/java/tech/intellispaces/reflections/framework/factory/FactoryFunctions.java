package tech.intellispaces.reflections.framework.factory;

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
import tech.intellispaces.core.Reflection;
import tech.intellispaces.javareflection.method.MethodStatement;
import tech.intellispaces.reflections.framework.annotation.Contract;

public interface FactoryFunctions {

  static String getContractType(MethodStatement method) {
    Optional<Contract> contract = method.selectAnnotation(Contract.class);
    if (contract.isPresent()) {
      return contract.get().value().getCanonicalName();
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

  @SuppressWarnings("unchecked")
  static  <R extends Reflection> Action1<R, PropertiesSet> createFactoryAction(
      FactoryMethod description
  ) {
    int numQualifiers = description.contractQualifierTypes().size();
    if (numQualifiers == 1) {
      Action1<R, Object> rootAction = (Action1<R, Object>) makeAction1(description);
      String qualifierName = description.contractQualifierNames().get(0);
      Type<?> qualifierType = description.contractQualifierTypes().get(0);
      if (qualifierType.asClassType().baseClass() == String.class) {
        Function<PropertiesSet, R> function = (PropertiesSet ps) -> {
          Object qualifierValue = ps.traverse(qualifierName);
          return rootAction.execute(qualifierValue);
        };
        return FunctionActions.ofFunction(function);
      }
    }
    throw NotImplementedExceptions.withCode("OSIoFg");
  }

  static Action makeAction(FactoryMethod description) {
    return switch (description.contractQualifierTypes().size()) {
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
  static Action0<?> makeAction0(FactoryMethod description) {
    var originAction = (Action1<Object, Object>) description.action();
    return originAction.convertToAction0(description.factoryInstance());
  }

  @SuppressWarnings("unchecked")
  static Action1<?, ?> makeAction1(FactoryMethod description) {
    var originAction = (Action2<Object, Object, Object>) description.action();
    return originAction.convertToAction1(description.factoryInstance());
  }

  @SuppressWarnings("unchecked")
  static Action2<?, ?, ?> makeAction2(FactoryMethod description) {
    var originAction = (Action3<Object, Object, Object, Object>) description.action();
    return originAction.convertToAction2(description.factoryInstance());
  }

  @SuppressWarnings("unchecked")
  static Action3<?, ?, ?, ?> makeAction3(FactoryMethod description) {
    var originAction = (Action4<Object, Object, Object, Object, Object>) description.action();
    return originAction.convertToAction3(description.factoryInstance());
  }

  @SuppressWarnings("unchecked")
  static Action4<?, ?, ?, ?, ?> makeAction4(FactoryMethod description) {
    var originAction = (Action5<Object, Object, Object, Object, Object, Object>) description.action();
    return originAction.convertToAction4(description.factoryInstance());
  }

  @SuppressWarnings("unchecked")
  static Action5<?, ?, ?, ?, ?, ?> makeAction5(FactoryMethod description) {
    var originAction = (Action6<Object, Object, Object, Object, Object, Object, Object>) description.action();
    return originAction.convertToAction5(description.factoryInstance());
  }

  @SuppressWarnings("unchecked")
  static Action6<?, ?, ?, ?, ?, ?, ?> makeAction6(FactoryMethod description) {
    var originAction = (Action7<Object, Object, Object, Object, Object, Object, Object, Object>) description.action();
    return originAction.convertToAction6(description.factoryInstance());
  }

  @SuppressWarnings("unchecked")
  static Action7<?, ?, ?, ?, ?, ?, ?, ?> makeAction7(FactoryMethod description) {
    var originAction = (Action8<Object, Object, Object, Object, Object, Object, Object, Object, Object>) description.action();
    return originAction.convertToAction7(description.factoryInstance());
  }

  @SuppressWarnings("unchecked")
  static Action8<?, ?, ?, ?, ?, ?, ?, ?, ?> makeAction8(FactoryMethod description) {
    var originAction = (Action9<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object>) description.action();
    return originAction.convertToAction8(description.factoryInstance());
  }

  @SuppressWarnings("unchecked")
  static Action9<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> makeAction9(FactoryMethod description) {
    var originAction = (Action10<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object>) description.action();
    return originAction.convertToAction9(description.factoryInstance());
  }
}
