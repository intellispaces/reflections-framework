package intellispaces.framework.core.system.kernel;

import intellispaces.framework.core.system.Module;

import java.util.List;

/**
 * Internal system module presentation.
 */
public interface SystemModule extends Module {

  SystemUnit mainUnit();

  List<SystemUnit> units();

  ProjectionRegistry projectionRegistry();

  GuideRegistry guideRegistry();

  ObjectRegistry objectRegistry();
}
