package intellispaces.jaquarius.system.injection;

import intellispaces.jaquarius.system.AutoGuideInjection;

public interface AutoGuideInjections {

  static AutoGuideInjection get(Class<?> unitClass, String name, Class<?> guideClass) {
    return new AutoGuideInjectionImpl(unitClass, name, guideClass);
  }
}
