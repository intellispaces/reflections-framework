package tech.intellispacesframework.core.system;

import tech.intellispacesframework.commons.action.ActionBuilders;
import tech.intellispacesframework.commons.action.Getter;
import tech.intellispacesframework.core.guide.AutoMover0;
import tech.intellispacesframework.core.guide.AutoMover1;
import tech.intellispacesframework.core.guide.n0.Mover0;
import tech.intellispacesframework.core.guide.n1.Mover1;
import tech.intellispacesframework.core.traverse.DeclarativeTraversePlan;
import tech.intellispacesframework.core.traverse.TraverseAnalyzer;
import tech.intellispacesframework.core.traverse.TraverseExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Default implementation of the {@link Module}.
 */
public class ModuleDefault implements Module {
  private final List<Unit> units;
  private final ProjectionRegistryDefault projectionRegistry;
  private final TraverseAnalyzer traverseAnalyzer;
  private final TraverseExecutor traverseExecutor;

  private final AtomicBoolean started = new AtomicBoolean(false);
  private final Getter<Unit> mainUnitGetter = ActionBuilders.cachedLazyGetter(this::mainUnitSupplier);

  private static final Logger LOG = LoggerFactory.getLogger(ModuleDefault.class);

  public ModuleDefault(
      List<Unit> units,
      ProjectionRegistryDefault projectionRegistry,
      TraverseAnalyzer traverseAnalyzer,
      TraverseExecutor traverseExecutor
  ) {
    this.units = List.copyOf(units);
    this.projectionRegistry = projectionRegistry;
    this.traverseAnalyzer = traverseAnalyzer;
    this.traverseExecutor = traverseExecutor;
  }

  ProjectionRegistryDefault projectionRegistry() {
    return projectionRegistry;
  }

  TraverseAnalyzer traverseAnalyzer() {
    return traverseAnalyzer;
  }

  TraverseExecutor traverseExecutor() {
    return traverseExecutor;
  }

  Unit mainUnit() {
    return mainUnitGetter.get();
  }

  private Unit mainUnitSupplier() {
    return units.stream()
        .filter(Unit::isMain)
        .findFirst()
        .orElseThrow();
  }

  @Override
  public List<Unit> units() {
    return units;
  }

  @Override
  public Collection<SystemProjection> projections() {
    return projectionRegistry.loadedProjections();
  }

  @Override
  public void shutdown() {
    if (started.compareAndSet(true, false)) {

    } else {
      LOG.warn("Module is already shutdown");
    }
  }

  void setStarted() {
    started.set(true);
  }

  @Override
  public <T> T projection(String name, Class<T> targetClass) {
    return projectionRegistry.projection(name, targetClass);
  }

  @Override
  public <S> Mover0<S> autoMoverThruTransition0(Class<S> sourceClass, String tid) {
    DeclarativeTraversePlan traversePlan = traverseAnalyzer.buildTraversePlanMoveObjectHandleThruTransition0(sourceClass, tid);
    return new AutoMover0<>(traversePlan, traverseExecutor);
  }

  @Override
  public <S, Q> Mover1<S, Q> autoMoverThruTransition1(Class<S> sourceClass, String tid) {
    DeclarativeTraversePlan traversePlan = traverseAnalyzer.buildTraversePlanMoveObjectHandleThruTransition1(sourceClass, tid);
    return new AutoMover1<>(traversePlan, traverseExecutor);
  }

  @Override
  public <S, Q> S moveThruTransition1(S source, String tid, Q qualifier) {
    return null;
  }
}
