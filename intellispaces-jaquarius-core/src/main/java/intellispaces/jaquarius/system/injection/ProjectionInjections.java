package intellispaces.jaquarius.system.injection;

import intellispaces.jaquarius.system.ProjectionInjection;

public interface ProjectionInjections {

  static ProjectionInjection get(Class<?> unitClass, String name, Class<?> targetClass) {
    return new ProjectionInjectionImpl(unitClass, name, targetClass);
  }
}
