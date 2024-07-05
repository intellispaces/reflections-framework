package tech.intellispaces.framework.core.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.intellispaces.framework.commons.action.ActionBuilders;
import tech.intellispaces.framework.commons.action.Getter;
import tech.intellispaces.framework.core.guide.n0.AutoMover0;
import tech.intellispaces.framework.core.guide.n0.Mover0;
import tech.intellispaces.framework.core.guide.n1.AutoMover1;
import tech.intellispaces.framework.core.guide.n1.Mover1;
import tech.intellispaces.framework.core.object.ObjectFunctions;
import tech.intellispaces.framework.core.traverse.DeclarativePlan;
import tech.intellispaces.framework.core.traverse.TraverseAnalyzer;
import tech.intellispaces.framework.core.traverse.TraverseExecutor;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Implementation of the {@link ModuleDefault}.
 */
public class ModuleDefaultImpl implements ModuleDefault {
  private final List<Unit> units;
  private final ProjectionRegistry projectionRegistry;
  private final TraverseAnalyzer traverseAnalyzer;
  private final TraverseExecutor traverseExecutor;

  private final AtomicBoolean started = new AtomicBoolean(false);
  private final Getter<Unit> mainUnitGetter = ActionBuilders.cachedLazyGetter(this::mainUnitSupplier);

  private static final Logger LOG = LoggerFactory.getLogger(ModuleDefaultImpl.class);

  public ModuleDefaultImpl(
      List<Unit> units,
      ProjectionRegistry projectionRegistry,
      TraverseAnalyzer traverseAnalyzer,
      TraverseExecutor traverseExecutor
  ) {
    this.units = List.copyOf(units);
    this.projectionRegistry = projectionRegistry;
    this.traverseAnalyzer = traverseAnalyzer;
    this.traverseExecutor = traverseExecutor;
  }

  @Override
  public ProjectionRegistry projectionRegistry() {
    return projectionRegistry;
  }

  TraverseAnalyzer traverseAnalyzer() {
    return traverseAnalyzer;
  }

  TraverseExecutor traverseExecutor() {
    return traverseExecutor;
  }

  @Override
  public Unit mainUnit() {
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
  public void start(String[] args) {
    ModuleStarterFunctions.startModule(this);
    started.set(true);
  }

  @Override
  public void stop() {
    if (started.compareAndSet(true, false)) {

    } else {
      LOG.warn("Module is already shutdown");
    }
  }

  @Override
  public <S, B> Mover0<S, B> autoMoverThruTransition0(Class<S> sourceClass, String tid) {
    DeclarativePlan traversePlan = traverseAnalyzer.buildTraversePlanMoveObjectHandleThruTransition0(
        sourceClass, tid);
    return new AutoMover0<>(tid, traversePlan, traverseExecutor);
  }

  @Override
  public <S, B, Q> Mover1<S, B, Q> autoMoverThruTransition1(Class<S> sourceClass, String tid) {
    DeclarativePlan traversePlan = traverseAnalyzer.buildTraversePlanMoveObjectHandleThruTransition1(
        sourceClass, tid);
    return new AutoMover1<>(tid, traversePlan, traverseExecutor);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, T> T mapThruTransition0(S source, String tid) {
    DeclarativePlan traversePlan = traverseAnalyzer.buildTraversePlanMapObjectHandleThruTransition0(
        ObjectFunctions.seekObjectHandleClass(source.getClass()), tid);
    return (T) traversePlan.execute(source, traverseExecutor);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, T, Q> T mapThruTransition1(S source, String tid, Q qualifier) {
    DeclarativePlan traversePlan = traverseAnalyzer.buildTraversePlanMapObjectHandleThruTransition1(
        ObjectFunctions.seekObjectHandleClass(source.getClass()), tid);
    return (T) traversePlan.execute(source, qualifier, traverseExecutor);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, B> B moveThruTransition0(S source, String tid) {
    DeclarativePlan traversePlan = traverseAnalyzer.buildTraversePlanMoveObjectHandleThruTransition0(
        ObjectFunctions.seekObjectHandleClass(source.getClass()), tid);
    return (B) traversePlan.execute(source, traverseExecutor);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, B, Q> B moveThruTransition1(S source, String tid, Q qualifier) {
    DeclarativePlan traversePlan = traverseAnalyzer.buildTraversePlanMoveObjectHandleThruTransition1(
        ObjectFunctions.seekObjectHandleClass(source.getClass()), tid);
    return (B) traversePlan.execute(source, qualifier, traverseExecutor);
  }
}
