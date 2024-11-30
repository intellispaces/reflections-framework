package tech.intellispaces.jaquarius.system.projection;

import tech.intellispaces.action.Action;
import tech.intellispaces.entity.exception.NotImplementedExceptions;
import tech.intellispaces.jaquarius.system.ProjectionDefinitionKind;
import tech.intellispaces.jaquarius.system.ProjectionDefinitionKinds;
import tech.intellispaces.jaquarius.system.ProjectionReference;
import tech.intellispaces.jaquarius.system.UnitProjectionDefinition;

import java.lang.reflect.Method;
import java.util.List;

public class ProjectionDefinitionBasedOnMethodAction implements UnitProjectionDefinition {
  private final Class<?> unitClass;
  private final String name;
  private final Class<?> type;
  private final boolean lazy;
  private final Action methodAction;
  private final List<ProjectionReference> requiredProjections;

  ProjectionDefinitionBasedOnMethodAction(
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
    return ProjectionDefinitionKinds.ProjectionDefinitionBasedOnUnitMethod;
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
