package tech.intellispaces.framework.core.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.intellispaces.framework.commons.action.Actions;
import tech.intellispaces.framework.commons.action.Getter;
import tech.intellispaces.framework.commons.type.Type;
import tech.intellispaces.framework.core.guide.n0.AutoMapper0;
import tech.intellispaces.framework.core.guide.n0.AutoMover0;
import tech.intellispaces.framework.core.guide.n0.Mapper0;
import tech.intellispaces.framework.core.guide.n0.Mover0;
import tech.intellispaces.framework.core.guide.n1.AutoMapper1;
import tech.intellispaces.framework.core.guide.n1.AutoMover1;
import tech.intellispaces.framework.core.guide.n1.Mapper1;
import tech.intellispaces.framework.core.guide.n1.Mover1;
import tech.intellispaces.framework.core.guide.n2.AutoMapper2;
import tech.intellispaces.framework.core.guide.n2.AutoMover2;
import tech.intellispaces.framework.core.guide.n2.Mapper2;
import tech.intellispaces.framework.core.guide.n2.Mover2;
import tech.intellispaces.framework.core.object.ObjectFunctions;
import tech.intellispaces.framework.core.space.transition.Transition0;
import tech.intellispaces.framework.core.space.transition.Transition1;
import tech.intellispaces.framework.core.space.transition.Transition2;
import tech.intellispaces.framework.core.space.transition.TransitionFunctions;
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
  private final Getter<Unit> mainUnitGetter = Actions.cachedLazyGetter(this::mainUnitSupplier);

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
      LOG.warn("Module is already stopped");
    }
  }

  @Override
  public <S, B> Mover0<S, B> autoMoverThruTransition0(Type<S> sourceType, String tid) {
    DeclarativePlan traversePlan = traverseAnalyzer.buildTraversePlanMoveObjectHandleThruTransition0(
        sourceType.superClass(), tid
    );
    return new AutoMover0<>(tid, traversePlan, traverseExecutor);
  }

  @Override
  public <S, B, TRANSITION extends Transition0<? super S, ? super S>> Mover0<S, B> autoMoverThruTransition0(
      Type<S> sourceType, Class<TRANSITION> transitionClass
  ) {
    return autoMoverThruTransition0(sourceType, TransitionFunctions.getTransitionId(transitionClass));
  }

  @Override
  public <S, B, Q> Mover1<S, B, Q> autoMoverThruTransition1(Type<S> sourceType, String tid) {
    DeclarativePlan traversePlan = traverseAnalyzer.buildTraversePlanMoveObjectHandleThruTransition1(
        sourceType.superClass(), tid
    );
    return new AutoMover1<>(tid, traversePlan, traverseExecutor);
  }

  @Override
  public <S, B, Q, TRANSITION extends Transition1<? super S, ? super S, ? super Q>> Mover1<S, B, Q> autoMoverThruTransition1(
      Type<S> sourceType, Class<TRANSITION> transitionClass
  ) {
    return autoMoverThruTransition1(sourceType, TransitionFunctions.getTransitionId(transitionClass));
  }

  @Override
  public <S, B, Q1, Q2> Mover2<S, B, Q1, Q2> autoMoverThruTransition2(Type<S> sourceType, String tid) {
    DeclarativePlan traversePlan = traverseAnalyzer.buildTraversePlanMoveObjectHandleThruTransition2(
        sourceType.superClass(), tid
    );
    return new AutoMover2<>(tid, traversePlan, traverseExecutor);
  }

  @Override
  public <S, B, Q1, Q2, TRANSITION extends Transition2<? super S, ? super S, ? super Q1, ? super Q2>> Mover2<S, B, Q1, Q2> autoMoverThruTransition2(
      Type<S> sourceType, Class<TRANSITION> transitionClass
  ) {
    return autoMoverThruTransition2(sourceType, TransitionFunctions.getTransitionId(transitionClass));
  }

  @Override
  public <S, T> Mapper0<S, T> autoMapperThruTransition0(Type<S> sourceType, String tid) {
    DeclarativePlan traversePlan = traverseAnalyzer.buildTraversePlanMapObjectHandleThruTransition0(
        sourceType.superClass(), tid
    );
    return new AutoMapper0<>(tid, traversePlan, traverseExecutor);
  }

  @Override
  public <S, T, TRANSITION extends Transition0<? super S, ? super T>> Mapper0<S, T> autoMapperThruTransition0(
      Type<S> sourceType, Class<TRANSITION> transitionClass
  ) {
    return autoMapperThruTransition0(sourceType, TransitionFunctions.getTransitionId(transitionClass));
  }

  @Override
  public <S, T, Q> Mapper1<S, T, Q> autoMapperThruTransition1(Type<S> sourceType, String tid) {
    DeclarativePlan traversePlan = traverseAnalyzer.buildTraversePlanMapObjectHandleThruTransition1(
        sourceType.superClass(), tid
    );
    return new AutoMapper1<>(tid, traversePlan, traverseExecutor);
  }

  @Override
  public <S, T, Q, TRANSITION extends Transition1<? super S, ? super T, ? super Q>> Mapper1<S, T, Q> autoMapperThruTransition1(
      Type<S> sourceType, Class<TRANSITION> transitionClass
  ) {
    return autoMapperThruTransition1(sourceType, TransitionFunctions.getTransitionId(transitionClass));
  }

  @Override
  public <S, T, Q1, Q2> Mapper2<S, T, Q1, Q2> autoMapperThruTransition2(Type<S> sourceType, String tid) {
    DeclarativePlan traversePlan = traverseAnalyzer.buildTraversePlanMapObjectHandleThruTransition2(
        sourceType.superClass(), tid
    );
    return new AutoMapper2<>(tid, traversePlan, traverseExecutor);
  }

  @Override
  public <S, T, Q1, Q2, TRANSITION extends Transition2<? super S, ? super T, ? super Q1, ? super Q2>> Mapper2<S, T, Q1, Q2> autoMapperThruTransition2(
      Type<S> sourceType, Class<TRANSITION> transitionClass
  ) {
    return autoMapperThruTransition2(sourceType, TransitionFunctions.getTransitionId(transitionClass));
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, T> T mapThruTransition0(S source, String tid) {
    DeclarativePlan traversePlan = traverseAnalyzer.buildTraversePlanMapObjectHandleThruTransition0(
        ObjectFunctions.defineObjectHandleClass(source.getClass()), tid);
    return (T) traversePlan.execute(source, traverseExecutor);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, T, Q> T mapThruTransition1(S source, String tid, Q qualifier) {
    DeclarativePlan traversePlan = traverseAnalyzer.buildTraversePlanMapObjectHandleThruTransition1(
        ObjectFunctions.defineObjectHandleClass(source.getClass()), tid);
    return (T) traversePlan.execute(source, qualifier, traverseExecutor);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, B> B moveThruTransition0(S source, String tid) {
    DeclarativePlan traversePlan = traverseAnalyzer.buildTraversePlanMoveObjectHandleThruTransition0(
        ObjectFunctions.defineObjectHandleClass(source.getClass()), tid);
    return (B) traversePlan.execute(source, traverseExecutor);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, B, Q> B moveThruTransition1(S source, String tid, Q qualifier) {
    DeclarativePlan traversePlan = traverseAnalyzer.buildTraversePlanMoveObjectHandleThruTransition1(
        ObjectFunctions.defineObjectHandleClass(source.getClass()), tid);
    return (B) traversePlan.execute(source, qualifier, traverseExecutor);
  }
}
