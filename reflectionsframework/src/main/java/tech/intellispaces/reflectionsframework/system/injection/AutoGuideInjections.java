package tech.intellispaces.reflectionsframework.system.injection;

import tech.intellispaces.reflectionsframework.system.AutoGuideInjection;

public interface AutoGuideInjections {

  static AutoGuideInjection get(Class<?> unitClass, String name, Class<?> guideClass) {
    return new AutoGuideInjectionImpl(unitClass, name, guideClass);
  }
}
