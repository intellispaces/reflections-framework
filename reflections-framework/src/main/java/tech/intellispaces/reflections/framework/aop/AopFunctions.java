package tech.intellispaces.reflections.framework.aop;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import tech.intellispaces.actions.Action;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.jstatements.method.MethodStatement;
import tech.intellispaces.reflections.framework.annotation.ApplyAdvice;
import tech.intellispaces.reflections.framework.engine.Engine;

public interface AopFunctions {

  @SuppressWarnings("unchecked, rawtypes")
  static Action buildChainAction(MethodStatement method, Action action, Engine engine) {
    List<ApplyAdvice> applyAdviceAnnotations = method.annotations().stream()
        .map(a -> a.annotationStatement().selectAnnotation(ApplyAdvice.class).orElse(null))
        .filter(Objects::nonNull)
        .toList();

    Set<Class<? extends Advice>> adviceClasses = (Set) applyAdviceAnnotations.stream()
        .filter(a -> a.adviceClass() != null)
        .collect(Collectors.groupingBy(ApplyAdvice::adviceClass))
        .keySet();
    Action currentAction = action;
    for (Class<?> adviceClass : adviceClasses) {
      try {
        Constructor constructor = adviceClass.getConstructor(
            MethodStatement.class, Action.class, Engine.class
        );
        currentAction = (Action) constructor.newInstance(
            method,
            currentAction,
            engine
        );
      } catch (Exception e) {
        throw UnexpectedExceptions.withCauseAndMessage(e, "Could not create AOP advice");
      }
    }
    return currentAction;
  }
}
