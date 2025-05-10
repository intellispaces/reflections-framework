package tech.intellispaces.reflections.framework.system.injection;

import tech.intellispaces.reflections.framework.system.ProjectionInjection;

public interface ProjectionInjections {

  static ProjectionInjection get(Class<?> unitClass, String name, Class<?> targetClass) {
    return new ProjectionInjectionImpl(unitClass, name, targetClass);
  }
}
