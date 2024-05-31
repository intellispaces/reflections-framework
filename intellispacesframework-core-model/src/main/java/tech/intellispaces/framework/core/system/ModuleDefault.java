package tech.intellispaces.framework.core.system;

import java.util.List;

/**
 * Default system module.
 */
public interface ModuleDefault extends Module {

  Unit mainUnit();

  List<Unit> units();

  ProjectionRegistry projectionRegistry();
}
