package tech.intellispaces.reflections.framework.system.injection;

import tech.intellispaces.reflections.framework.system.GuideInjection;

public interface GuideInjections {

  static GuideInjection get(Class<?> unitClass, String name, Class<?> guideClass) {
    return new GuideInjectionImpl(unitClass, name, guideClass);
  }
}
