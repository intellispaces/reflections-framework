package tech.intellispaces.jaquarius.system.kernel;

import tech.intellispaces.jaquarius.system.Module;
import tech.intellispaces.jaquarius.traverse.plan.TraverseAnalyzer;
import tech.intellispaces.jaquarius.traverse.plan.TraverseExecutor;

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
