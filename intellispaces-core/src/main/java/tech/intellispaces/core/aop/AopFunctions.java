package tech.intellispaces.core.aop;

import tech.intellispaces.actions.Action;
import tech.intellispaces.commons.exception.UnexpectedViolationException;
import tech.intellispaces.core.annotation.ApplyAdvice;
import tech.intellispaces.javastatements.method.MethodStatement;
import tech.intellispaces.javastatements.method.Methods;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public interface AopFunctions {

  static Action buildChainAction(Method method, Action terminalAction) {
    return buildChainAction(Methods.of(method), terminalAction);
  }

  @SuppressWarnings("unchecked, rawtypes")
  static Action buildChainAction(MethodStatement method, Action terminalAction) {
    List<ApplyAdvice> applyAdviceAnnotations = method.annotations().stream()
        .map(a -> a.annotationStatement().selectAnnotation(ApplyAdvice.class).orElse(null))
        .filter(Objects::nonNull)
        .toList();

    Set<Class<? extends Advice>> adviceClasses = (Set) applyAdviceAnnotations.stream()
        .filter(a -> a.adviceClass() != null)
        .collect(Collectors.groupingBy(ApplyAdvice::adviceClass))
        .keySet();
    Action currentAction = terminalAction;
    for (Class<?> adviceClass : adviceClasses) {
      try {
        Constructor constructor = adviceClass.getConstructor(MethodStatement.class, Action.class);
        currentAction = (Action) constructor.newInstance(method, currentAction);
      } catch (Exception e) {
        throw UnexpectedViolationException.withCauseAndMessage(e, "Could not create AOP advice");
      }
    }
    return currentAction;
  }
}
