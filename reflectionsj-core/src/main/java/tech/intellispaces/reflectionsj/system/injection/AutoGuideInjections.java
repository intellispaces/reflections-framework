package tech.intellispaces.reflectionsj.system.injection;

import tech.intellispaces.reflectionsj.system.AutoGuideInjection;

public interface AutoGuideInjections {

  static AutoGuideInjection get(Class<?> unitClass, String name, Class<?> guideClass) {
    return new AutoGuideInjectionImpl(unitClass, name, guideClass);
  }
}
