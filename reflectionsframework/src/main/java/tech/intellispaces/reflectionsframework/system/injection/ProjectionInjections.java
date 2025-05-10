package tech.intellispaces.reflectionsframework.system.injection;

import tech.intellispaces.reflectionsframework.system.ProjectionInjection;

public interface ProjectionInjections {

  static ProjectionInjection get(Class<?> unitClass, String name, Class<?> targetClass) {
    return new ProjectionInjectionImpl(unitClass, name, targetClass);
  }
}
