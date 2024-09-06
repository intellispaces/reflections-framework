package intellispaces.framework.core.system;

import java.util.List;

/**
 * Module unit.
 */
public interface Unit {

  boolean isMain();

  Class<?> unitClass();

  List<UnitProjectionDefinition> projectionDefinitions();

  List<AttachedUnitGuide> guides();
}
