package intellispaces.framework.core.system.kernel;

import intellispaces.framework.core.system.Module;

import java.util.List;

/**
 * Internal kernel module presentation.
 */
public interface KernelModule extends Module {

  KernelUnit mainUnit();

  List<KernelUnit> units();

  ProjectionRegistry projectionRegistry();

  GuideRegistry guideRegistry();

  ObjectRegistry objectRegistry();
}
