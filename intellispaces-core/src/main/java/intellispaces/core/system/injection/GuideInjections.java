package intellispaces.core.system.injection;

import intellispaces.core.system.GuideInjection;

public interface GuideInjections {

  static GuideInjection get(Class<?> unitClass, String name, Class<?> guideClass) {
    return new GuideInjectionImpl(unitClass, name, guideClass);
  }
}
