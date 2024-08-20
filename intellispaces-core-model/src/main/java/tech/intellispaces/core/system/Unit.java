package tech.intellispaces.core.system;

import tech.intellispaces.core.guide.Guide;

import java.util.List;

/**
 * Module unit.
 */
public interface Unit {

  boolean isMain();

  Class<?> unitClass();

  Object instance();

  List<Injection> injections();

  List<UnitProjectionDefinition> projectionDefinitions();

  List<Guide<?, ?>> guides();
}
