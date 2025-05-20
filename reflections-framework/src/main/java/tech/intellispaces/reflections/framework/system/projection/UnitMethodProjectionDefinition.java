package tech.intellispaces.reflections.framework.system.projection;

import java.lang.reflect.Method;
import java.util.List;

import tech.intellispaces.actions.Action;
import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.reflections.framework.system.ProjectionDefinitionKind;
import tech.intellispaces.reflections.framework.system.ProjectionReference;
import tech.intellispaces.reflections.framework.system.UnitProjectionDefinition;

public class UnitMethodProjectionDefinition implements UnitProjectionDefinition {
  private final Class<?> unitClass;
  private final String name;
  private final Class<?> type;
  private final boolean lazy;
  private final Action methodAction;
  private final List<ProjectionReference> requiredProjections;

  UnitMethodProjectionDefinition(
      Class<?> unitClass,
      String name,
      Class<?> type,
      boolean lazy,
      Action methodAction,
      List<ProjectionReference> requiredProjections
  ) {
    this.unitClass = unitClass;
    this.name = name;
    this.type = type;
    this.lazy = lazy;
    this.methodAction = methodAction;
    this.requiredProjections = requiredProjections;
  }

  @Override
  public ProjectionDefinitionKind kind() {
    return ProjectionDefinitionKinds.UnitMethodProjectionDefinition;
  }

  @Override
  public Class<?> unitClass() {
    return unitClass;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public Class<?> type() {
    return type;
  }

  @Override
  public boolean isLazy() {
    return lazy;
  }

  public Action methodAction() {
    return methodAction;
  }

  public List<ProjectionReference> requiredProjections() {
    return requiredProjections;
  }

  @Override
  public Method projectionMethod() {
    throw NotImplementedExceptions.withCode("p3QJww");
  }
}
