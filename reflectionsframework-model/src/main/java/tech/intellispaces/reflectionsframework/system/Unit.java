package tech.intellispaces.reflectionsframework.system;

import java.util.List;

/**
 * The module unit.
 */
public interface Unit {

  boolean isMain();

  Class<?> unitClass();

  List<UnitProjectionDefinition> projectionDefinitions();

  List<UnitGuide<?, ?>> guides();
}
