package tech.intellispaces.core.system.shadow;

import tech.intellispaces.core.system.Module;
import tech.intellispaces.core.system.ObjectRegistry;
import tech.intellispaces.core.system.ProjectionRegistry;

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
