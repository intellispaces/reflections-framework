package tech.intellispaces.jaquarius.system.injection;

import tech.intellispaces.jaquarius.system.GuideInjection;

public interface GuideInjections {

  static GuideInjection get(Class<?> unitClass, String name, Class<?> guideClass) {
    return new GuideInjectionImpl(unitClass, name, guideClass);
  }
}
