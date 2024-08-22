package tech.intellispaces.core.system.projection;

import tech.intellispaces.core.system.UnitProjectionInjection;

public interface UnitProjectionInjections {

  static UnitProjectionInjection get(Class<?> unitClass, String name, Class<?> targetClass) {
    return new UnitProjectionInjectionImpl(unitClass, name, targetClass);
  }
}
