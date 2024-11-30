package tech.intellispaces.jaquarius.aop;

import tech.intellispaces.action.Action;
import tech.intellispaces.entity.exception.UnexpectedExceptions;
import tech.intellispaces.jaquarius.annotation.ApplyAdvice;
import tech.intellispaces.jaquarius.system.ProjectionProvider;
import tech.intellispaces.jaquarius.system.kernel.ProjectionRegistry;
import tech.intellispaces.java.reflection.method.MethodStatement;
import tech.intellispaces.java.reflection.method.Methods;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public interface AopFunctions {

  static Action buildChainAction(Method method, Action action, ProjectionRegistry projectionRegistry) {
    return buildChainAction(Methods.of(method), action, projectionRegistry);
  }

  @SuppressWarnings("unchecked, rawtypes")
  static Action buildChainAction(MethodStatement method, Action action, ProjectionRegistry projectionRegistry) {
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
            projectionRegistry
        );
      } catch (Exception e) {
        throw UnexpectedExceptions.withCauseAndMessage(e, "Could not create AOP advice");
      }
    }
    return currentAction;
  }
}
