package intellispaces.framework.core.system.kernel;

import intellispaces.common.action.Actions;
import intellispaces.common.action.getter.Getter;
import intellispaces.common.javastatement.type.Type;
import intellispaces.common.javastatement.type.Types;
import intellispaces.framework.core.guide.n0.AutoMapper0;
import intellispaces.framework.core.guide.n0.AutoMover0;
import intellispaces.framework.core.guide.n0.Mapper0;
import intellispaces.framework.core.guide.n0.Mover0;
import intellispaces.framework.core.guide.n1.AutoMapper1;
import intellispaces.framework.core.guide.n1.AutoMover1;
import intellispaces.framework.core.guide.n1.Mapper1;
import intellispaces.framework.core.guide.n1.Mover1;
import intellispaces.framework.core.guide.n2.AutoMapper2;
import intellispaces.framework.core.guide.n2.AutoMover2;
import intellispaces.framework.core.guide.n2.Mapper2;
import intellispaces.framework.core.guide.n2.Mover2;
import intellispaces.framework.core.guide.n3.AutoMapper3;
import intellispaces.framework.core.guide.n3.AutoMover3;
import intellispaces.framework.core.guide.n3.Mapper3;
import intellispaces.framework.core.guide.n3.Mover3;
import intellispaces.framework.core.object.ObjectFunctions;
import intellispaces.framework.core.space.transition.Transition0;
import intellispaces.framework.core.space.transition.Transition1;
import intellispaces.framework.core.space.transition.Transition2;
import intellispaces.framework.core.space.transition.Transition3;
import intellispaces.framework.core.space.transition.TransitionFunctions;
import intellispaces.framework.core.system.ModuleProjection;
import intellispaces.framework.core.system.Unit;
import intellispaces.framework.core.traverse.DeclarativePlan;
import intellispaces.framework.core.traverse.TraverseAnalyzer;
import intellispaces.framework.core.traverse.TraverseExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Implementation of the {@link KernelModule}.
 */
class ModuleImpl implements KernelModule {
  private final List<KernelUnit> units;
  private final ObjectRegistry objectRegistry;
  private final ProjectionRegistry projectionRegistry;
  private final GuideRegistry guideRegistry;
  private final TraverseAnalyzer traverseAnalyzer;
  private final TraverseExecutor traverseExecutor;

  private final AtomicBoolean started = new AtomicBoolean(false);
  private final Getter<KernelUnit> mainUnitGetter = Actions.cachedLazyGetter(this::mainUnitSupplier);

  private static final Logger LOG = LoggerFactory.getLogger(ModuleImpl.class);

  ModuleImpl(
      List<KernelUnit> units,
      ObjectRegistry objectRegistry,
      ProjectionRegistry projectionRegistry,
      GuideRegistry guideRegistry,
      TraverseAnalyzer traverseAnalyzer,
      TraverseExecutor traverseExecutor
  ) {
    this.units = List.copyOf(units);
    this.objectRegistry = objectRegistry;
    this.projectionRegistry = projectionRegistry;
    this.guideRegistry = guideRegistry;
    this.traverseAnalyzer = traverseAnalyzer;
    this.traverseExecutor = traverseExecutor;
  }

  @Override
  public ObjectRegistry objectRegistry() {
    return objectRegistry;
  }

  @Override
  public ProjectionRegistry projectionRegistry() {
    return projectionRegistry;
  }

  @Override
  public GuideRegistry guideRegistry() {
    return guideRegistry;
  }

  TraverseAnalyzer traverseAnalyzer() {
    return traverseAnalyzer;
  }

  TraverseExecutor traverseExecutor() {
    return traverseExecutor;
  }

  @Override
  public KernelUnit mainUnit() {
    return mainUnitGetter.get();
  }

  private KernelUnit mainUnitSupplier() {
    return units.stream()
        .filter(Unit::isMain)
        .findFirst()
        .orElseThrow();
  }

  @Override
  public List<KernelUnit> units() {
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

  @Override
  public <S, T> Mapper0<S, T> autoMapperThruTransition0(Type<S> sourceType, String tid) {
    DeclarativePlan traversePlan = traverseAnalyzer.buildTraversePlanMapObjectHandleThruTransition0(
        sourceType.baseClass(), tid
    );
    return new AutoMapper0<>(tid, traversePlan, traverseExecutor);
  }

  @Override
  public <S, T, Q> Mapper1<S, T, Q> autoMapperThruTransition1(Type<S> sourceType, String tid) {
    DeclarativePlan traversePlan = traverseAnalyzer.buildTraversePlanMapObjectHandleThruTransition1(
        sourceType.baseClass(), tid
    );
    return new AutoMapper1<>(tid, traversePlan, traverseExecutor);
  }

  @Override
  public <S, T, Q1, Q2> Mapper2<S, T, Q1, Q2> autoMapperThruTransition2(Type<S> sourceType, String tid) {
    DeclarativePlan traversePlan = traverseAnalyzer.buildTraversePlanMapObjectHandleThruTransition2(
        sourceType.baseClass(), tid
    );
    return new AutoMapper2<>(tid, traversePlan, traverseExecutor);
  }

  @Override
  public <S, T, Q1, Q2, Q3> Mapper3<S, T, Q1, Q2, Q3> autoMapperThruTransition3(Type<S> sourceType, String tid) {
    DeclarativePlan traversePlan = traverseAnalyzer.buildTraversePlanMapObjectHandleThruTransition3(
      sourceType.baseClass(), tid
    );
    return new AutoMapper3<>(tid, traversePlan, traverseExecutor);
  }

  @Override
  public <S, B> Mover0<S, B> autoMoverThruTransition0(Type<S> sourceType, String tid) {
    DeclarativePlan traversePlan = traverseAnalyzer.buildTraversePlanMoveObjectHandleThruTransition0(
        sourceType.baseClass(), tid
    );
    return new AutoMover0<>(tid, traversePlan, traverseExecutor);
  }

  @Override
  public <S, B, Q> Mover1<S, B, Q> autoMoverThruTransition1(Class<S> sourceClass, String tid) {
    return autoMoverThruTransition1(Types.of(sourceClass), tid);
  }

  @Override
  public <S, B, Q> Mover1<S, B, Q> autoMoverThruTransition1(Type<S> sourceType, String tid) {
    DeclarativePlan traversePlan = traverseAnalyzer.buildTraversePlanMoveObjectHandleThruTransition1(
        sourceType.baseClass(), tid
    );
    return new AutoMover1<>(tid, traversePlan, traverseExecutor);
  }

  @Override
  public <S, B, Q1, Q2> Mover2<S, B, Q1, Q2> autoMoverThruTransition2(Type<S> sourceType, String tid) {
    DeclarativePlan traversePlan = traverseAnalyzer.buildTraversePlanMoveObjectHandleThruTransition2(
        sourceType.baseClass(), tid
    );
    return new AutoMover2<>(tid, traversePlan, traverseExecutor);
  }

  @Override
  public <S, B, Q1, Q2, Q3> Mover3<S, B, Q1, Q2, Q3> autoMoverThruTransition3(Type<S> sourceType, String tid) {
    DeclarativePlan traversePlan = traverseAnalyzer.buildTraversePlanMoveObjectHandleThruTransition3(
      sourceType.baseClass(), tid
    );
    return new AutoMover3<>(tid, traversePlan, traverseExecutor);
  }

  @Override
  public <S, T> Mapper0<S, T> autoMapperThruTransition0(
      Type<S> sourceType, Class<? extends Transition0> transitionClass
  ) {
    return autoMapperThruTransition0(sourceType, TransitionFunctions.getTransitionId(transitionClass));
  }

  @Override
  @SuppressWarnings("rawtypes")
  public <S, T, Q> Mapper1<S, T, Q> autoMapperThruTransition1(
      Type<S> sourceType, Class<? extends Transition1> transitionClass
  ) {
    return autoMapperThruTransition1(sourceType, TransitionFunctions.getTransitionId(transitionClass));
  }

  @Override
  @SuppressWarnings("rawtypes")
  public <S, T, Q1, Q2> Mapper2<S, T, Q1, Q2> autoMapperThruTransition2(
      Type<S> sourceType, Class<? extends Transition2> transitionClass
  ) {
    return autoMapperThruTransition2(sourceType, TransitionFunctions.getTransitionId(transitionClass));
  }

  @Override
  @SuppressWarnings("rawtypes")
  public <S, T, Q1, Q2, Q3> Mapper3<S, T, Q1, Q2, Q3> autoMapperThruTransition3(
    Type<S> sourceType, Class<? extends Transition3> transitionClass
  ) {
    return autoMapperThruTransition3(sourceType, TransitionFunctions.getTransitionId(transitionClass));
  }

  @Override
  @SuppressWarnings("rawtypes")
  public <S, B> Mover0<S, B> autoMoverThruTransition0(
      Type<S> sourceType, Class<? extends Transition0> transitionClass
  ) {
    return autoMoverThruTransition0(sourceType, TransitionFunctions.getTransitionId(transitionClass));
  }

  @Override
  @SuppressWarnings("rawtypes")
  public <S, B, Q> Mover1<S, B, Q> autoMoverThruTransition1(
      Type<S> sourceType, Class<? extends Transition1> transitionClass
  ) {
    return autoMoverThruTransition1(sourceType, TransitionFunctions.getTransitionId(transitionClass));
  }

  @Override
  @SuppressWarnings("rawtypes")
  public <S, B, Q1, Q2> Mover2<S, B, Q1, Q2> autoMoverThruTransition2(
      Type<S> sourceType, Class<? extends Transition2> transitionClass
  ) {
    return autoMoverThruTransition2(sourceType, TransitionFunctions.getTransitionId(transitionClass));
  }

  @Override
  @SuppressWarnings("rawtypes")
  public <S, B, Q1, Q2, Q3> Mover3<S, B, Q1, Q2, Q3> autoMoverThruTransition3(
    Type<S> sourceType, Class<? extends Transition3> transitionClass
  ) {
    return autoMoverThruTransition3(sourceType, TransitionFunctions.getTransitionId(transitionClass));
  }

  @Override
  public <T> T getProjection(String name, Class<T> targetClass) {
    return projectionRegistry.getProjection(name, targetClass);
  }

  @Override
  public <T> List<T> getProjections(Class<T> targetClass) {
    return projectionRegistry.getProjections(targetClass);
  }

  @Override
  public Collection<ModuleProjection> allProjections() {
    return projectionRegistry.allProjections();
  }
}
