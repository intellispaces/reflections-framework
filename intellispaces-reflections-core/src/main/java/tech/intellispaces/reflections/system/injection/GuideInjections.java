package tech.intellispaces.reflections.system.injection;

import tech.intellispaces.reflections.system.GuideInjection;

public interface GuideInjections {

  static GuideInjection get(Class<?> unitClass, String name, Class<?> guideClass) {
    return new GuideInjectionImpl(unitClass, name, guideClass);
  }
}
