package intellispaces.core.system.shadow;

import intellispaces.core.system.Module;
import intellispaces.core.system.ObjectRegistry;
import intellispaces.core.system.ProjectionRegistry;

import java.util.List;

/**
 * Internal module presentation.
 */
public interface ShadowModule extends Module {

  ShadowUnit mainUnit();

  List<ShadowUnit> units();

  ProjectionRegistry projectionRegistry();

  ObjectRegistry objectRegistry();
}
