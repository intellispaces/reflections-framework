package tech.intellispaces.reflections.framework.system;

import java.util.List;

/**
 * The module unit.
 */
public interface Unit {

  boolean isMain();

  List<UnitGuide<?, ?>> guides();
}
