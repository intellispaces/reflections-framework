package tech.intellispaces.reflectionsj.aop;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import tech.intellispaces.actions.Action;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.reflectionsj.annotation.ApplyAdvice;
import tech.intellispaces.reflectionsj.system.ProjectionProvider;
import tech.intellispaces.statementsj.method.MethodStatement;

public interface AopFunctions {

  @SuppressWarnings("unchecked, rawtypes")
  static Action buildChainAction(MethodStatement method, Action action, ProjectionProvider projectionProvider) {
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
            MethodStatement.class, Action.class, ProjectionProvider.class
        );
        currentAction = (Action) constructor.newInstance(
            method,
            currentAction,
            projectionProvider
        );
      } catch (Exception e) {
        throw UnexpectedExceptions.withCauseAndMessage(e, "Could not create AOP advice");
      }
    }
    return currentAction;
  }
}
