package tech.intellispaces.core.system.projection;

import tech.intellispaces.core.system.ProjectionInjection;

class ProjectionInjectionImpl implements ProjectionInjection {
  private final String name;
  private final Class<?> targetClass;

  ProjectionInjectionImpl(String name, Class<?> targetClass) {
    this.name = name;
    this.targetClass = targetClass;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public Class<?> targetClass() {
    return targetClass;
  }
}
