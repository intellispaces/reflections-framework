package tech.intellispaces.reflectionsj.system.injection;

import tech.intellispaces.reflectionsj.system.GuideInjection;

public interface GuideInjections {

  static GuideInjection get(Class<?> unitClass, String name, Class<?> guideClass) {
    return new GuideInjectionImpl(unitClass, name, guideClass);
  }
}
