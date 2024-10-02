package intellispaces.framework.core.system.kernel;

import intellispaces.common.action.Actions;
import intellispaces.common.action.getter.Getter;
import intellispaces.common.base.type.Type;
import intellispaces.common.javastatement.type.Types;
import intellispaces.framework.core.guide.n0.AutoMapper0;
import intellispaces.framework.core.guide.n0.AutoMapperOfMoving0;
import intellispaces.framework.core.guide.n0.AutoMover0;
import intellispaces.framework.core.guide.n0.Mapper0;
import intellispaces.framework.core.guide.n0.MapperOfMoving0;
import intellispaces.framework.core.guide.n0.Mover0;
import intellispaces.framework.core.guide.n1.AutoMapper1;
import intellispaces.framework.core.guide.n1.AutoMapperOfMoving1;
import intellispaces.framework.core.guide.n1.AutoMover1;
import intellispaces.framework.core.guide.n1.Mapper1;
import intellispaces.framework.core.guide.n1.MapperOfMoving1;
import intellispaces.framework.core.guide.n1.Mover1;
import intellispaces.framework.core.guide.n2.AutoMapper2;
import intellispaces.framework.core.guide.n2.AutoMapperOfMoving2;
import intellispaces.framework.core.guide.n2.AutoMover2;
import intellispaces.framework.core.guide.n2.Mapper2;
import intellispaces.framework.core.guide.n2.MapperOfMoving2;
import intellispaces.framework.core.guide.n2.Mover2;
import intellispaces.framework.core.guide.n3.AutoMapper3;
import intellispaces.framework.core.guide.n3.AutoMapperOfMoving3;
import intellispaces.framework.core.guide.n3.AutoMover3;
import intellispaces.framework.core.guide.n3.Mapper3;
import intellispaces.framework.core.guide.n3.MapperOfMoving3;
import intellispaces.framework.core.guide.n3.Mover3;
import intellispaces.framework.core.object.ObjectFunctions;
import intellispaces.framework.core.space.channel.Channel0;
import intellispaces.framework.core.space.channel.Channel1;
import intellispaces.framework.core.space.channel.Channel2;
import intellispaces.framework.core.space.channel.Channel3;
import intellispaces.framework.core.space.channel.ChannelFunctions;
import intellispaces.framework.core.system.ModuleProjection;
import intellispaces.framework.core.system.Unit;
import intellispaces.framework.core.traverse.plan.DeclarativePlan;
import intellispaces.framework.core.traverse.plan.TraverseAnalyzer;
import intellispaces.framework.core.traverse.plan.TraverseExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Implementation of the {@link KernelModule}.
 */
class KernelModuleImpl implements KernelModule {
  private final List<KernelUnit> units;
  private final ObjectRegistry objectRegistry;
  private final ProjectionRegistry projectionRegistry;
  private final GuideRegistry guideRegistry;
  private final TraverseAnalyzer traverseAnalyzer;
  private final TraverseExecutor traverseExecutor;

  private final AtomicBoolean started = new AtomicBoolean(false);
  private final Getter<KernelUnit> mainUnitGetter = Actions.cachedLazyGetter(this::mainUnitSupplier);

  private static final Logger LOG = LoggerFactory.getLogger(KernelModuleImpl.class);

  KernelModuleImpl(
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
  public <S, T> T mapThruChannel0(S source, String cid) {
    DeclarativePlan traversePlan = traverseAnalyzer.buildMapObjectHandleThruChannel0Plan(
        ObjectFunctions.defineObjectHandleClass(source.getClass()), cid);
    return (T) traversePlan.execute(source, traverseExecutor);
  }

  @Override
  @SuppressWarnings("rawtypes")
  public <S, T> T mapThruChannel0(S source, Class<? extends Channel0> channelClass) {
    return mapThruChannel0(source, ChannelFunctions.getChannelId(channelClass));
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, T, Q> T mapThruChannel1(S source, String cid, Q qualifier) {
    DeclarativePlan traversePlan = traverseAnalyzer.buildMapObjectHandleThruChannel1Plan(
        ObjectFunctions.defineObjectHandleClass(source.getClass()), cid);
    return (T) traversePlan.execute(source, qualifier, traverseExecutor);
  }

  @Override
  @SuppressWarnings("rawtypes")
  public <S, T, Q> T mapThruChannel1(S source, Class<? extends Channel1> channelClass, Q qualifier) {
    return mapThruChannel1(source, ChannelFunctions.getChannelId(channelClass), qualifier);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, R> R moveThruChannel0(S source, String cid) {
    DeclarativePlan traversePlan = traverseAnalyzer.buildMoveObjectHandleThruChannel0Plan(
        ObjectFunctions.defineObjectHandleClass(source.getClass()), cid);
    return (R) traversePlan.execute(source, traverseExecutor);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, R, Q> R moveThruChannel1(S source, String cid, Q qualifier) {
    DeclarativePlan traversePlan = traverseAnalyzer.buildMoveObjectHandleThruChannel1Plan(
        ObjectFunctions.defineObjectHandleClass(source.getClass()), cid);
    return (R) traversePlan.execute(source, qualifier, traverseExecutor);
  }

  @Override
  public <S, T> Mapper0<S, T> autoMapperThruChannel0(Type<S> sourceType, String cid) {
    DeclarativePlan traversePlan = traverseAnalyzer.buildMapObjectHandleThruChannel0Plan(
        sourceType.baseClass(), cid
    );
    return new AutoMapper0<>(cid, traversePlan, traverseExecutor);
  }

  @Override
  public <S, T, Q> Mapper1<S, T, Q> autoMapperThruChannel1(Type<S> sourceType, String cid) {
    DeclarativePlan traversePlan = traverseAnalyzer.buildMapObjectHandleThruChannel1Plan(
        sourceType.baseClass(), cid
    );
    return new AutoMapper1<>(cid, traversePlan, traverseExecutor);
  }

  @Override
  public <S, T, Q1, Q2> Mapper2<S, T, Q1, Q2> autoMapperThruChannel2(Type<S> sourceType, String cid) {
    DeclarativePlan traversePlan = traverseAnalyzer.buildMapObjectHandleThruChannel2Plan(
        sourceType.baseClass(), cid
    );
    return new AutoMapper2<>(cid, traversePlan, traverseExecutor);
  }

  @Override
  public <S, T, Q1, Q2, Q3> Mapper3<S, T, Q1, Q2, Q3> autoMapperThruChannel3(Type<S> sourceType, String cid) {
    DeclarativePlan traversePlan = traverseAnalyzer.buildMapObjectHandleThruChannel3Plan(
      sourceType.baseClass(), cid
    );
    return new AutoMapper3<>(cid, traversePlan, traverseExecutor);
  }

  @Override
  public <S> Mover0<S> autoMoverThruChannel0(Type<S> sourceType, String cid) {
    DeclarativePlan traversePlan = traverseAnalyzer.buildMoveObjectHandleThruChannel0Plan(
        sourceType.baseClass(), cid
    );
    return new AutoMover0<>(cid, traversePlan, traverseExecutor);
  }

  @Override
  public <S, Q> Mover1<S, Q> autoMoverThruChannel1(Class<S> sourceClass, String cid) {
    return autoMoverThruChannel1(Types.of(sourceClass), cid);
  }

  @Override
  public <S, Q> Mover1<S, Q> autoMoverThruChannel1(Type<S> sourceType, String cid) {
    DeclarativePlan traversePlan = traverseAnalyzer.buildMoveObjectHandleThruChannel1Plan(
        sourceType.baseClass(), cid
    );
    return new AutoMover1<>(cid, traversePlan, traverseExecutor);
  }

  @Override
  public <S, Q1, Q2> Mover2<S, Q1, Q2> autoMoverThruChannel2(Type<S> sourceType, String cid) {
    DeclarativePlan traversePlan = traverseAnalyzer.buildMoveObjectHandleThruChannel2Plan(
        sourceType.baseClass(), cid
    );
    return new AutoMover2<>(cid, traversePlan, traverseExecutor);
  }

  @Override
  public <S, Q1, Q2, Q3> Mover3<S, Q1, Q2, Q3> autoMoverThruChannel3(Type<S> sourceType, String cid) {
    DeclarativePlan traversePlan = traverseAnalyzer.buildMoveObjectHandleThruChannel3Plan(
      sourceType.baseClass(), cid
    );
    return new AutoMover3<>(cid, traversePlan, traverseExecutor);
  }

  @Override
  public <S, T> MapperOfMoving0<S, T> autoMapperOfMovingThruChannel0(Type<S> sourceType, String cid) {
    DeclarativePlan traversePlan = traverseAnalyzer.buildMapOfMovingObjectHandleThruChannel0Plan(
        sourceType.baseClass(), cid
    );
    return new AutoMapperOfMoving0<>(cid, traversePlan, traverseExecutor);
  }

  @Override
  public <S, T, Q> MapperOfMoving1<S, T, Q> autoMapperOfMovingThruChannel1(Type<S> sourceType, String cid) {
    DeclarativePlan traversePlan = traverseAnalyzer.buildMapOfMovingObjectHandleThruChannel1Plan(
        sourceType.baseClass(), cid
    );
    return new AutoMapperOfMoving1<>(cid, traversePlan, traverseExecutor);
  }

  @Override
  public <S, T, Q1, Q2> MapperOfMoving2<S, T, Q1, Q2> autoMapperOfMovingThruChannel2(Type<S> sourceType, String cid) {
    DeclarativePlan traversePlan = traverseAnalyzer.buildMapOfMovingObjectHandleThruChannel2Plan(
        sourceType.baseClass(), cid
    );
    return new AutoMapperOfMoving2<>(cid, traversePlan, traverseExecutor);
  }

  @Override
  public <S, T, Q1, Q2, Q3> MapperOfMoving3<S, T, Q1, Q2, Q3> autoMapperOfMovingThruChannel3(Type<S> sourceType, String cid) {
    DeclarativePlan traversePlan = traverseAnalyzer.buildMapOfMovingObjectHandleThruChannel3Plan(
        sourceType.baseClass(), cid
    );
    return new AutoMapperOfMoving3<>(cid, traversePlan, traverseExecutor);
  }

  @Override
  public <S, T> Mapper0<S, T> autoMapperThruChannel0(
      Type<S> sourceType, Class<? extends Channel0> channelClass
  ) {
    return autoMapperThruChannel0(sourceType, ChannelFunctions.getChannelId(channelClass));
  }

  @Override
  @SuppressWarnings("rawtypes")
  public <S, T, Q> Mapper1<S, T, Q> autoMapperThruChannel1(
      Type<S> sourceType, Class<? extends Channel1> channelClass
  ) {
    return autoMapperThruChannel1(sourceType, ChannelFunctions.getChannelId(channelClass));
  }

  @Override
  @SuppressWarnings("rawtypes")
  public <S, T, Q1, Q2> Mapper2<S, T, Q1, Q2> autoMapperThruChannel2(
      Type<S> sourceType, Class<? extends Channel2> channelClass
  ) {
    return autoMapperThruChannel2(sourceType, ChannelFunctions.getChannelId(channelClass));
  }

  @Override
  @SuppressWarnings("rawtypes")
  public <S, T, Q1, Q2, Q3> Mapper3<S, T, Q1, Q2, Q3> autoMapperThruChannel3(
    Type<S> sourceType, Class<? extends Channel3> channelClass
  ) {
    return autoMapperThruChannel3(sourceType, ChannelFunctions.getChannelId(channelClass));
  }

  @Override
  @SuppressWarnings("rawtypes")
  public <S> Mover0<S> autoMoverThruChannel0(
      Type<S> sourceType, Class<? extends Channel0> channelClass
  ) {
    return autoMoverThruChannel0(sourceType, ChannelFunctions.getChannelId(channelClass));
  }

  @Override
  @SuppressWarnings("rawtypes")
  public <S, Q> Mover1<S, Q> autoMoverThruChannel1(
      Type<S> sourceType, Class<? extends Channel1> channelClass
  ) {
    return autoMoverThruChannel1(sourceType, ChannelFunctions.getChannelId(channelClass));
  }

  @Override
  @SuppressWarnings("rawtypes")
  public <S, Q1, Q2> Mover2<S, Q1, Q2> autoMoverThruChannel2(
      Type<S> sourceType, Class<? extends Channel2> channelClass
  ) {
    return autoMoverThruChannel2(sourceType, ChannelFunctions.getChannelId(channelClass));
  }

  @Override
  @SuppressWarnings("rawtypes")
  public <S, Q1, Q2, Q3> Mover3<S, Q1, Q2, Q3> autoMoverThruChannel3(
    Type<S> sourceType, Class<? extends Channel3> channelClass
  ) {
    return autoMoverThruChannel3(sourceType, ChannelFunctions.getChannelId(channelClass));
  }

  @Override
  public <S, T> MapperOfMoving0<S, T> autoMapperOfMovingThruChannel0(
      Type<S> sourceType, Class<? extends Channel0> channelClass
  ) {
    return autoMapperOfMovingThruChannel0(sourceType, ChannelFunctions.getChannelId(channelClass));
  }

  @Override
  @SuppressWarnings("rawtypes")
  public <S, T, Q> MapperOfMoving1<S, T, Q> autoMapperOfMovingThruChannel1(
      Type<S> sourceType, Class<? extends Channel1> channelClass
  ) {
    return autoMapperOfMovingThruChannel1(sourceType, ChannelFunctions.getChannelId(channelClass));
  }

  @Override
  @SuppressWarnings("rawtypes")
  public <S, T, Q1, Q2> MapperOfMoving2<S, T, Q1, Q2> autoMapperOfMovingThruChannel2(
      Type<S> sourceType, Class<? extends Channel2> channelClass
  ) {
    return autoMapperOfMovingThruChannel2(sourceType, ChannelFunctions.getChannelId(channelClass));
  }

  @Override
  @SuppressWarnings("rawtypes")
  public <S, T, Q1, Q2, Q3> MapperOfMoving3<S, T, Q1, Q2, Q3> autoMapperOfMovingThruChannel3(
      Type<S> sourceType, Class<? extends Channel3> channelClass
  ) {
    return autoMapperOfMovingThruChannel3(sourceType, ChannelFunctions.getChannelId(channelClass));
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
