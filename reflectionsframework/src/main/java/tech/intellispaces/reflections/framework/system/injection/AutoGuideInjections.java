package tech.intellispaces.reflections.framework.system.injection;

import tech.intellispaces.reflections.framework.system.AutoGuideInjection;

public interface AutoGuideInjections {

  static AutoGuideInjection get(Class<?> unitClass, String name, Class<?> guideClass) {
    return new AutoGuideInjectionImpl(unitClass, name, guideClass);
  }
}
