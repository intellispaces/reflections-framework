package intellispaces.core.system.kernel;

import intellispaces.core.system.Module;

import java.util.List;

/**
 * Internal module presentation.
 */
public interface KernelModule extends Module {

  KernelUnit mainUnit();

  List<KernelUnit> units();

  ProjectionRegistry projectionRegistry();

  GuideRegistry guideRegistry();

  ObjectRegistry objectRegistry();
}
