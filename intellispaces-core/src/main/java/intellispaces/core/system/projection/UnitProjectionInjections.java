package intellispaces.core.system.projection;

import intellispaces.core.system.UnitProjectionInjection;

public interface UnitProjectionInjections {

  static UnitProjectionInjection get(Class<?> unitClass, String name, Class<?> targetClass) {
    return new UnitProjectionInjectionImpl(unitClass, name, targetClass);
  }
}
