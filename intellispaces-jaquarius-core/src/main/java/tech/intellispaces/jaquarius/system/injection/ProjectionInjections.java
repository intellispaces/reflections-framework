package tech.intellispaces.jaquarius.system.injection;

import tech.intellispaces.jaquarius.system.ProjectionInjection;

public interface ProjectionInjections {

  static ProjectionInjection get(Class<?> unitClass, String name, Class<?> targetClass) {
    return new ProjectionInjectionImpl(unitClass, name, targetClass);
  }
}
