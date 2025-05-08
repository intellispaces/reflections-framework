package tech.intellispaces.reflections.system.injection;

import tech.intellispaces.reflections.system.AutoGuideInjection;

public interface AutoGuideInjections {

  static AutoGuideInjection get(Class<?> unitClass, String name, Class<?> guideClass) {
    return new AutoGuideInjectionImpl(unitClass, name, guideClass);
  }
}
