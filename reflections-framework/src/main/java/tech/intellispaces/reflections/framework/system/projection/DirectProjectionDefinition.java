package tech.intellispaces.reflections.framework.system.projection;

import tech.intellispaces.reflections.framework.system.ProjectionDefinition;
import tech.intellispaces.reflections.framework.system.ProjectionDefinitionKind;

public class DirectProjectionDefinition implements ProjectionDefinition {
  private final String name;
  private final Class<?> type;
  private final Object target;

  DirectProjectionDefinition(String name, Class<?> type, Object target) {
    this.name = name;
    this.type = type;
    this.target = target;
  }

  @Override
  public ProjectionDefinitionKind kind() {
    return ProjectionDefinitionKinds.DirectProjectionDefinition;
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
    return false;
  }

  public Object target() {
    return target;
  }
}
