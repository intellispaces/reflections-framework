package tech.intellispaces.reflections.framework.engine.impl;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tech.intellispaces.actions.cache.CachedSupplierActions;
import tech.intellispaces.actions.supplier.SupplierAction;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.commons.type.Types;
import tech.intellispaces.reflections.framework.channel.Channel0;
import tech.intellispaces.reflections.framework.channel.Channel1;
import tech.intellispaces.reflections.framework.channel.Channel2;
import tech.intellispaces.reflections.framework.channel.Channel3;
import tech.intellispaces.reflections.framework.channel.Channel4;
import tech.intellispaces.reflections.framework.guide.n0.AutoMapper0;
import tech.intellispaces.reflections.framework.guide.n0.AutoMapperOfMoving0;
import tech.intellispaces.reflections.framework.guide.n0.AutoMover0;
import tech.intellispaces.reflections.framework.guide.n0.Mapper0;
import tech.intellispaces.reflections.framework.guide.n0.MapperOfMoving0;
import tech.intellispaces.reflections.framework.guide.n0.Mover0;
import tech.intellispaces.reflections.framework.guide.n1.AutoMapper1;
import tech.intellispaces.reflections.framework.guide.n1.AutoMapperOfMoving1;
import tech.intellispaces.reflections.framework.guide.n1.AutoMover1;
import tech.intellispaces.reflections.framework.guide.n1.Mapper1;
import tech.intellispaces.reflections.framework.guide.n1.MapperOfMoving1;
import tech.intellispaces.reflections.framework.guide.n1.Mover1;
import tech.intellispaces.reflections.framework.guide.n2.AutoMapper2;
import tech.intellispaces.reflections.framework.guide.n2.AutoMapperOfMoving2;
import tech.intellispaces.reflections.framework.guide.n2.AutoMover2;
import tech.intellispaces.reflections.framework.guide.n2.Mapper2;
import tech.intellispaces.reflections.framework.guide.n2.MapperOfMoving2;
import tech.intellispaces.reflections.framework.guide.n2.Mover2;
import tech.intellispaces.reflections.framework.guide.n3.AutoMapper3;
import tech.intellispaces.reflections.framework.guide.n3.AutoMapperOfMoving3;
import tech.intellispaces.reflections.framework.guide.n3.AutoMover3;
import tech.intellispaces.reflections.framework.guide.n3.Mapper3;
import tech.intellispaces.reflections.framework.guide.n3.MapperOfMoving3;
import tech.intellispaces.reflections.framework.guide.n3.Mover3;
import tech.intellispaces.reflections.framework.guide.n4.AutoMapperOfMoving4;
import tech.intellispaces.reflections.framework.guide.n4.MapperOfMoving4;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.reflection.ReflectionForms;
import tech.intellispaces.reflections.framework.reflection.ReflectionFunctions;
import tech.intellispaces.reflections.framework.space.channel.ChannelFunctions;
import tech.intellispaces.reflections.framework.system.LocalGuideRegistry;
import tech.intellispaces.reflections.framework.system.Module;
import tech.intellispaces.reflections.framework.system.ProjectionRegistry;
import tech.intellispaces.reflections.framework.system.UnitHandle;
import tech.intellispaces.reflections.framework.traverse.MappingOfMovingTraverse;
import tech.intellispaces.reflections.framework.traverse.MappingTraverse;
import tech.intellispaces.reflections.framework.traverse.plan.DeclarativeTraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.TraverseAnalyzer;
import tech.intellispaces.reflections.framework.traverse.plan.TraverseExecutor;
import tech.intellispaces.reflections.framework.traverse.plan.TraversePlan;

class ModuleImpl implements Module {
  private final List<UnitHandle> units;
  private final ProjectionRegistry projectionRegistry;
  private final LocalGuideRegistry guideRegistry;
  private final TraverseAnalyzer traverseAnalyzer;
  private final TraverseExecutor traverseExecutor;

  private final AtomicBoolean started = new AtomicBoolean(false);
  private final SupplierAction<UnitHandle> mainUnitGetter = CachedSupplierActions.get(this::mainUnitSupplier);

  private static final Logger LOG = LoggerFactory.getLogger(ModuleImpl.class);

  ModuleImpl(
      List<UnitHandle> units,
      ProjectionRegistry projectionRegistry,
      LocalGuideRegistry guideRegistry,
      TraverseAnalyzer traverseAnalyzer,
      TraverseExecutor traverseExecutor
  ) {
    this.units = List.copyOf(units);
    this.projectionRegistry = projectionRegistry;
    this.guideRegistry = guideRegistry;
    this.traverseAnalyzer = traverseAnalyzer;
    this.traverseExecutor = traverseExecutor;
  }

  @Override
  public void start() {
    start(new String[] {});
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

  public ProjectionRegistry projectionRegistry() {
    return projectionRegistry;
  }

  public LocalGuideRegistry guideRegistry() {
    return guideRegistry;
  }

  public TraverseAnalyzer traverseAnalyzer() {
    return traverseAnalyzer;
  }

  public TraverseExecutor traverseExecutor() {
    return traverseExecutor;
  }

  public UnitHandle mainUnit() {
    return mainUnitGetter.get();
  }

  public List<UnitHandle> units() {
    return units;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, T> T mapThruChannel0(S source, String cid) {
    DeclarativeTraversePlan traversePlan = traverseAnalyzer.buildMapThruChannel0Plan(
        ReflectionFunctions.getReflectionClass(source.getClass()), cid, ReflectionForms.Reflection);
    return (T) traversePlan.execute(source, traverseExecutor);
  }

  @Override
  public <S, T, C extends Channel0 & MappingTraverse> T mapThruChannel0(S source, Class<C> channelClass) {
    return mapThruChannel0(source, ChannelFunctions.getChannelId(channelClass));
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, T, Q> T mapThruChannel1(S source, String cid, Q qualifier) {
    DeclarativeTraversePlan traversePlan = traverseAnalyzer.buildMapThruChannel1Plan(
        ReflectionFunctions.getReflectionClass(source.getClass()), cid, ReflectionForms.Reflection);
    return (T) traversePlan.execute(source, qualifier, traverseExecutor);
  }

  @Override
  public <S, T, Q, C extends Channel1 & MappingTraverse> T mapThruChannel1(S source, Class<C> channelClass, Q qualifier) {
    return mapThruChannel1(source, ChannelFunctions.getChannelId(channelClass), qualifier);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, R> R moveThruChannel0(S source, String cid) {
    TraversePlan traversePlan = traverseAnalyzer.buildMoveThruChannel0Plan(
        ReflectionFunctions.getReflectionClass(source.getClass()), cid, ReflectionForms.Reflection);
    return (R) traversePlan.execute(source, traverseExecutor);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, R, Q> R moveThruChannel1(S source, String cid, Q qualifier) {
    TraversePlan traversePlan = traverseAnalyzer.buildMoveThruChannel1Plan(
        ReflectionFunctions.getReflectionClass(source.getClass()), cid, ReflectionForms.Reflection);
    return (R) traversePlan.execute(source, qualifier, traverseExecutor);
  }

  @Override
  public <S, R, Q, C extends Channel1 & MappingOfMovingTraverse> R mapOfMovingThruChannel1(
      S source, Class<C> channelClass, Q qualifier
  ) {
    return mapOfMovingThruChannel1(source, ChannelFunctions.getChannelId(channelClass), qualifier);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, R, Q> R mapOfMovingThruChannel1(S source, String cid, Q qualifier) {
    TraversePlan traversePlan = traverseAnalyzer.buildMapOfMovingThruChannel1Plan(
        ReflectionFunctions.getReflectionClass(source.getClass()), cid, ReflectionForms.Reflection);
    return (R) traversePlan.execute(source, qualifier, traverseExecutor);
  }

  @Override
  public <S, T> Mapper0<S, T> autoMapperThruChannel0(Type<S> sourceType, String cid, ReflectionForm targetForm) {
    TraversePlan traversePlan = traverseAnalyzer.buildMapThruChannel0Plan(
        sourceType.asClassType().baseClass(), cid, targetForm
    );
    return new AutoMapper0<>(cid, traversePlan, targetForm, traverseExecutor);
  }

  @Override
  public <S, T, Q> Mapper1<S, T, Q> autoMapperThruChannel1(Type<S> sourceType, String cid, ReflectionForm targetForm) {
    TraversePlan traversePlan = traverseAnalyzer.buildMapThruChannel1Plan(
        sourceType.asClassType().baseClass(), cid, targetForm
    );
    return new AutoMapper1<>(cid, traversePlan, targetForm, traverseExecutor);
  }

  @Override
  public <S, T, Q1, Q2> Mapper2<S, T, Q1, Q2> autoMapperThruChannel2(Type<S> sourceType, String cid, ReflectionForm targetForm) {
    TraversePlan traversePlan = traverseAnalyzer.buildMapThruChannel2Plan(
        sourceType.asClassType().baseClass(), cid, targetForm
    );
    return new AutoMapper2<>(cid, traversePlan, targetForm, traverseExecutor);
  }

  @Override
  public <S, T, Q1, Q2, Q3> Mapper3<S, T, Q1, Q2, Q3> autoMapperThruChannel3(Type<S> sourceType, String cid, ReflectionForm targetForm) {
    TraversePlan traversePlan = traverseAnalyzer.buildMapThruChannel3Plan(
        sourceType.asClassType().baseClass(), cid, targetForm
    );
    return new AutoMapper3<>(cid, traversePlan, targetForm, traverseExecutor);
  }

  @Override
  public <S> Mover0<S> autoMoverThruChannel0(Type<S> sourceType, String cid, ReflectionForm targetForm) {
    TraversePlan traversePlan = traverseAnalyzer.buildMoveThruChannel0Plan(
        sourceType.asClassType().baseClass(), cid, targetForm
    );
    return new AutoMover0<>(cid, traversePlan, targetForm, traverseExecutor);
  }

  @Override
  public <S, Q> Mover1<S, Q> autoMoverThruChannel1(Class<S> sourceClass, String cid, ReflectionForm targetForm) {
    return autoMoverThruChannel1(Types.get(sourceClass), cid, targetForm);
  }

  @Override
  public <S, Q> Mover1<S, Q> autoMoverThruChannel1(Type<S> sourceType, String cid, ReflectionForm targetForm) {
    TraversePlan traversePlan = traverseAnalyzer.buildMoveThruChannel1Plan(
        sourceType.asClassType().baseClass(), cid, targetForm
    );
    return new AutoMover1<>(cid, traversePlan, targetForm, traverseExecutor);
  }

  @Override
  public <S, Q1, Q2> Mover2<S, Q1, Q2> autoMoverThruChannel2(Type<S> sourceType, String cid, ReflectionForm targetForm) {
    TraversePlan traversePlan = traverseAnalyzer.buildMoveThruChannel2Plan(
        sourceType.asClassType().baseClass(), cid, targetForm
    );
    return new AutoMover2<>(cid, traversePlan, targetForm, traverseExecutor);
  }

  @Override
  public <S, Q1, Q2, Q3> Mover3<S, Q1, Q2, Q3> autoMoverThruChannel3(Type<S> sourceType, String cid, ReflectionForm targetForm) {
    TraversePlan traversePlan = traverseAnalyzer.buildMoveThruChannel3Plan(
        sourceType.asClassType().baseClass(), cid, targetForm
    );
    return new AutoMover3<>(cid, traversePlan, targetForm, traverseExecutor);
  }

  @Override
  public <S, T> MapperOfMoving0<S, T> autoMapperOfMovingThruChannel0(Type<S> sourceType, String cid, ReflectionForm targetForm) {
    TraversePlan traversePlan = traverseAnalyzer.buildMapOfMovingThruChannel0Plan(
        sourceType.asClassType().baseClass(), cid, targetForm
    );
    return new AutoMapperOfMoving0<>(cid, traversePlan, targetForm, traverseExecutor);
  }

  @Override
  public <S, T, Q> MapperOfMoving1<S, T, Q> autoMapperOfMovingThruChannel1(Type<S> sourceType, String cid, ReflectionForm targetForm) {
    TraversePlan traversePlan = traverseAnalyzer.buildMapOfMovingThruChannel1Plan(
        sourceType.asClassType().baseClass(), cid, targetForm
    );
    return new AutoMapperOfMoving1<>(cid, traversePlan, targetForm, traverseExecutor);
  }

  @Override
  public <S, T, Q1, Q2> MapperOfMoving2<S, T, Q1, Q2> autoMapperOfMovingThruChannel2(Type<S> sourceType, String cid, ReflectionForm targetForm) {
    TraversePlan traversePlan = traverseAnalyzer.buildMapOfMovingThruChannel2Plan(
        sourceType.asClassType().baseClass(), cid, targetForm
    );
    return new AutoMapperOfMoving2<>(cid, traversePlan, targetForm, traverseExecutor);
  }

  @Override
  public <S, T, Q1, Q2, Q3> MapperOfMoving3<S, T, Q1, Q2, Q3> autoMapperOfMovingThruChannel3(
      Type<S> sourceType, String cid, ReflectionForm targetForm
  ) {
    TraversePlan traversePlan = traverseAnalyzer.buildMapOfMovingThruChannel3Plan(
        sourceType.asClassType().baseClass(), cid, targetForm
    );
    return new AutoMapperOfMoving3<>(cid, traversePlan, targetForm, traverseExecutor);
  }

  @Override
  public <S, T, Q1, Q2, Q3, Q4> MapperOfMoving4<S, T, Q1, Q2, Q3, Q4> autoMapperOfMovingThruChannel4(
      Type<S> sourceType, String cid, ReflectionForm targetForm
  ) {
    TraversePlan traversePlan = traverseAnalyzer.buildMapOfMovingThruChannel4Plan(
        sourceType.asClassType().baseClass(), cid, targetForm
    );
    return new AutoMapperOfMoving4<>(cid, traversePlan, targetForm, traverseExecutor);
  }

  @Override
  public <S, T> Mapper0<S, T> autoMapperThruChannel0(
      Type<S> sourceType, Class<? extends Channel0> channelClass, ReflectionForm targetForm
  ) {
    return autoMapperThruChannel0(sourceType, ChannelFunctions.getChannelId(channelClass), targetForm);
  }

  @Override
  public <S, T, Q> Mapper1<S, T, Q> autoMapperThruChannel1(
      Type<S> sourceType, Class<? extends Channel1> channelClass, ReflectionForm targetForm
  ) {
    return autoMapperThruChannel1(sourceType, ChannelFunctions.getChannelId(channelClass), targetForm);
  }

  @Override
  public <S, T, Q1, Q2> Mapper2<S, T, Q1, Q2> autoMapperThruChannel2(
      Type<S> sourceType, Class<? extends Channel2> channelClass, ReflectionForm targetForm
  ) {
    return autoMapperThruChannel2(sourceType, ChannelFunctions.getChannelId(channelClass), targetForm);
  }

  @Override
  public <S, T, Q1, Q2, Q3> Mapper3<S, T, Q1, Q2, Q3> autoMapperThruChannel3(
      Type<S> sourceType, Class<? extends Channel3> channelClass, ReflectionForm targetForm
  ) {
    return autoMapperThruChannel3(sourceType, ChannelFunctions.getChannelId(channelClass), targetForm);
  }

  @Override
  public <S> Mover0<S> autoMoverThruChannel0(
      Type<S> sourceType, Class<? extends Channel0> channelClass, ReflectionForm targetForm
  ) {
    return autoMoverThruChannel0(sourceType, ChannelFunctions.getChannelId(channelClass), targetForm);
  }

  @Override
  public <S, Q> Mover1<S, Q> autoMoverThruChannel1(
      Type<S> sourceType, Class<? extends Channel1> channelClass, ReflectionForm targetForm
  ) {
    return autoMoverThruChannel1(sourceType, ChannelFunctions.getChannelId(channelClass), targetForm);
  }

  @Override
  public <S, Q1, Q2> Mover2<S, Q1, Q2> autoMoverThruChannel2(
      Type<S> sourceType, Class<? extends Channel2> channelClass, ReflectionForm targetForm
  ) {
    return autoMoverThruChannel2(sourceType, ChannelFunctions.getChannelId(channelClass), targetForm);
  }

  @Override
  public <S, Q1, Q2, Q3> Mover3<S, Q1, Q2, Q3> autoMoverThruChannel3(
      Type<S> sourceType, Class<? extends Channel3> channelClass, ReflectionForm targetForm
  ) {
    return autoMoverThruChannel3(sourceType, ChannelFunctions.getChannelId(channelClass), targetForm);
  }

  @Override
  public <S, T> MapperOfMoving0<S, T> autoMapperOfMovingThruChannel0(
      Type<S> sourceType, Class<? extends Channel0> channelClass, ReflectionForm targetForm
  ) {
    return autoMapperOfMovingThruChannel0(sourceType, ChannelFunctions.getChannelId(channelClass), targetForm);
  }

  @Override
  public <S, T, Q> MapperOfMoving1<S, T, Q> autoMapperOfMovingThruChannel1(
      Type<S> sourceType, Class<? extends Channel1> channelClass, ReflectionForm targetForm
  ) {
    return autoMapperOfMovingThruChannel1(sourceType, ChannelFunctions.getChannelId(channelClass), targetForm);
  }

  @Override
  public <S, T, Q1, Q2> MapperOfMoving2<S, T, Q1, Q2> autoMapperOfMovingThruChannel2(
      Type<S> sourceType, Class<? extends Channel2> channelClass, ReflectionForm targetForm
  ) {
    return autoMapperOfMovingThruChannel2(sourceType, ChannelFunctions.getChannelId(channelClass), targetForm);
  }

  @Override
  public <S, T, Q1, Q2, Q3> MapperOfMoving3<S, T, Q1, Q2, Q3> autoMapperOfMovingThruChannel3(
      Type<S> sourceType, Class<? extends Channel3> channelClass, ReflectionForm targetForm
  ) {
    return autoMapperOfMovingThruChannel3(sourceType, ChannelFunctions.getChannelId(channelClass), targetForm);
  }

  @Override
  public <S, T, Q1, Q2, Q3, Q4> MapperOfMoving4<S, T, Q1, Q2, Q3, Q4> autoMapperOfMovingThruChannel4(
      Type<S> sourceType, Class<? extends Channel4> channelClass, ReflectionForm targetForm
  ) {
    return autoMapperOfMovingThruChannel4(sourceType, ChannelFunctions.getChannelId(channelClass), targetForm);
  }

  @Override
  public <G> G getGuide(String name, Class<G> guideClass) {
    return guideRegistry.getGuide(name, guideClass);
  }

  @Override
  public <G> G getAutoGuide(Class<G> guideClass) {
    return guideRegistry.getAutoGuide(guideClass);
  }

  @Override
  public <T> T getProjection(String name, Class<T> targetReflectionClass) {
    return projectionRegistry.getProjection(name, targetReflectionClass);
  }

  @Override
  public <T> List<T> getProjections(Class<T> targetReflectionClass) {
    return projectionRegistry.findProjections(targetReflectionClass);
  }

  @Override
  public <T> void addContextProjection(String name, Class<T> targetReflectionClass, T target) {
    projectionRegistry.addContextProjection(name, targetReflectionClass, target);
  }

  @Override
  public void removeContextProjection(String name) {
    projectionRegistry.removeContextProjection(name);
  }

  private UnitHandle mainUnitSupplier() {
    return units.stream()
        .filter(tech.intellispaces.reflections.framework.system.Unit::isMain)
        .findFirst()
        .orElseThrow();
  }
}
