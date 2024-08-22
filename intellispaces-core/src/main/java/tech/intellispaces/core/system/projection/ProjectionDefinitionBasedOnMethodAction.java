package tech.intellispaces.core.system.projection;

import tech.intellispaces.actions.Action;
import tech.intellispaces.core.system.ProjectionDefinitionKind;
import tech.intellispaces.core.system.ProjectionDefinitionKinds;
import tech.intellispaces.core.system.ProjectionInjection;
import tech.intellispaces.core.system.UnitProjectionDefinition;

import java.lang.reflect.Method;
import java.util.List;

public class ProjectionDefinitionBasedOnMethodAction implements UnitProjectionDefinition {
  private final Class<?> unitClass;
  private final String name;
  private final Class<?> type;
  private final boolean lazy;
  private final Action methodAction;
  private final List<ProjectionInjection> requiredProjections;

  ProjectionDefinitionBasedOnMethodAction(
      Class<?> unitClass,
      String name,
      Class<?> type,
      boolean lazy,
      Action methodAction,
      List<ProjectionInjection> requiredProjections
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

  public List<ProjectionInjection> requiredProjections() {
    return requiredProjections;
  }

  @Override
  public Method projectionMethod() {
    throw new RuntimeException("Not implemented");
  }
}
