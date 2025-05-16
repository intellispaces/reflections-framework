package tech.intellispaces.reflections.framework.system;

import java.util.List;

public interface UnitTypes {

  static UnitType create(
    Class<?> unitClass,
    List<UnitMethod> methods
  ) {
    return new UnitTypeImpl(unitClass, methods);
  }
}
