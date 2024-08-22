package intellispaces.core.system;

import intellispaces.core.guide.Guide;

import java.util.List;

/**
 * Module unit.
 */
public interface Unit {

  boolean isMain();

  Class<?> unitClass();

  List<UnitProjectionDefinition> projectionDefinitions();

  List<Guide<?, ?>> guides();
}
