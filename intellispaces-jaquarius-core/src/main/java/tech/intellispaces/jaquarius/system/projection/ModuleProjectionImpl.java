package tech.intellispaces.jaquarius.system.projection;

import tech.intellispaces.jaquarius.system.ModuleProjection;
import tech.intellispaces.jaquarius.system.ProjectionDefinition;

public class ModuleProjectionImpl implements ModuleProjection {
  private final String name;
  private final Class<?> type;
  private final Object target;
  private final ProjectionDefinition provider;

  public ModuleProjectionImpl(String name, Class<?> type, ProjectionDefinition definition, Object target) {
    this.name = name;
    this.type = type;
    this.provider = definition;
    this.target = target;
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
  public ProjectionDefinition definition() {
    return provider;
  }

  @Override
  public Object target() {
    return target;
  }
}
