package intellispaces.framework.core.system.kernel;

import intellispaces.framework.core.system.Module;
import intellispaces.framework.core.traverse.plan.TraverseAnalyzer;
import intellispaces.framework.core.traverse.plan.TraverseExecutor;

import java.util.List;

/**
 * Internal kernel module presentation.
 */
public interface KernelModule {

  default void start() {
    start(new String[] {});
  }

  void start(String[] args);

  void stop();

  Module module();

  KernelUnit mainUnit();

  List<KernelUnit> units();

  ProjectionRegistry projectionRegistry();

  GuideRegistry guideRegistry();

  ObjectRegistry objectRegistry();

  TraverseAnalyzer traverseAnalyzer();

  TraverseExecutor traverseExecutor();
}
