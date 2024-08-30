package intellispaces.core.system.injection;

import intellispaces.core.system.ProjectionInjection;

public interface ProjectionInjections {

  static ProjectionInjection get(Class<?> unitClass, String name, Class<?> targetClass) {
    return new ProjectionInjectionImpl(unitClass, name, targetClass);
  }
}
