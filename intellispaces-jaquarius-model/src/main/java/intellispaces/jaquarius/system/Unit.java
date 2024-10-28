package intellispaces.jaquarius.system;

import java.util.List;

/**
 * Module unit.
 */
public interface Unit {

  boolean isMain();

  Class<?> unitClass();

  List<UnitProjectionDefinition> projectionDefinitions();

  List<UnitGuide<?, ?>> guides();
}
