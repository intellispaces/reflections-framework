package tech.intellispaces.reflections.engine.impl;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tech.intellispaces.actions.cache.CachedSupplierActions;
import tech.intellispaces.actions.supplier.SupplierAction;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.commons.type.Types;
import tech.intellispaces.reflections.channel.Channel0;
import tech.intellispaces.reflections.channel.Channel1;
import tech.intellispaces.reflections.channel.Channel2;
import tech.intellispaces.reflections.channel.Channel3;
import tech.intellispaces.reflections.channel.Channel4;
import tech.intellispaces.reflections.guide.n0.AutoMapper0;
import tech.intellispaces.reflections.guide.n0.AutoMapperOfMoving0;
import tech.intellispaces.reflections.guide.n0.AutoMover0;
import tech.intellispaces.reflections.guide.n0.Mapper0;
import tech.intellispaces.reflections.guide.n0.MapperOfMoving0;
import tech.intellispaces.reflections.guide.n0.Mover0;
import tech.intellispaces.reflections.guide.n1.AutoMapper1;
import tech.intellispaces.reflections.guide.n1.AutoMapperOfMoving1;
import tech.intellispaces.reflections.guide.n1.AutoMover1;
import tech.intellispaces.reflections.guide.n1.Mapper1;
import tech.intellispaces.reflections.guide.n1.MapperOfMoving1;
import tech.intellispaces.reflections.guide.n1.Mover1;
import tech.intellispaces.reflections.guide.n2.AutoMapper2;
import tech.intellispaces.reflections.guide.n2.AutoMapperOfMoving2;
import tech.intellispaces.reflections.guide.n2.AutoMover2;
import tech.intellispaces.reflections.guide.n2.Mapper2;
import tech.intellispaces.reflections.guide.n2.MapperOfMoving2;
import tech.intellispaces.reflections.guide.n2.Mover2;
import tech.intellispaces.reflections.guide.n3.AutoMapper3;
import tech.intellispaces.reflections.guide.n3.AutoMapperOfMoving3;
import tech.intellispaces.reflections.guide.n3.AutoMover3;
import tech.intellispaces.reflections.guide.n3.Mapper3;
import tech.intellispaces.reflections.guide.n3.MapperOfMoving3;
import tech.intellispaces.reflections.guide.n3.Mover3;
import tech.intellispaces.reflections.guide.n4.AutoMapperOfMoving4;
import tech.intellispaces.reflections.guide.n4.MapperOfMoving4;
import tech.intellispaces.reflections.object.reference.ObjectReferenceForm;
import tech.intellispaces.reflections.object.reference.ObjectReferenceForms;
import tech.intellispaces.reflections.object.reference.ObjectReferenceFunctions;
import tech.intellispaces.reflections.space.channel.ChannelFunctions;
import tech.intellispaces.reflections.traverse.MappingOfMovingTraverse;
import tech.intellispaces.reflections.traverse.MappingTraverse;
import tech.intellispaces.reflections.traverse.plan.DeclarativeTraversePlan;
import tech.intellispaces.reflections.traverse.plan.TraverseAnalyzer;
import tech.intellispaces.reflections.traverse.plan.TraverseExecutor;
import tech.intellispaces.reflections.traverse.plan.TraversePlan;

class Module implements tech.intellispaces.reflections.system.Module {
  private final List<Unit> units;
  private final ProjectionRegistry projectionRegistry;
  private final GuideRegistry guideRegistry;
  private final TraverseAnalyzer traverseAnalyzer;
  private final TraverseExecutor traverseExecutor;

  private final AtomicBoolean started = new AtomicBoolean(false);
  private final SupplierAction<Unit> mainUnitGetter = CachedSupplierActions.get(
      this::mainUnitSupplier
  );

  private static final Logger LOG = LoggerFactory.getLogger(Module.class);

  Module(
      List<Unit> units,
      ProjectionRegistry projectionRegistry,
      GuideRegistry guideRegistry,
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

  public GuideRegistry guideRegistry() {
    return guideRegistry;
  }

  public TraverseAnalyzer traverseAnalyzer() {
    return traverseAnalyzer;
  }

  public TraverseExecutor traverseExecutor() {
    return traverseExecutor;
  }

  public Unit mainUnit() {
    return mainUnitGetter.get();
  }

  public List<Unit> units() {
    return units;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, T> T mapThruChannel0(S source, String cid) {
    DeclarativeTraversePlan traversePlan = traverseAnalyzer.buildMapObjectHandleThruChannel0Plan(
        ObjectReferenceFunctions.getObjectHandleClass(source.getClass()), cid, ObjectReferenceForms.ObjectHandle);
    return (T) traversePlan.execute(source, traverseExecutor);
  }

  @Override
  public <S, T, C extends Channel0 & MappingTraverse> T mapThruChannel0(S source, Class<C> channelClass) {
    return mapThruChannel0(source, ChannelFunctions.getChannelId(channelClass));
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, T, Q> T mapThruChannel1(S source, String cid, Q qualifier) {
    DeclarativeTraversePlan traversePlan = traverseAnalyzer.buildMapObjectHandleThruChannel1Plan(
        ObjectReferenceFunctions.getObjectHandleClass(source.getClass()), cid, ObjectReferenceForms.ObjectHandle);
    return (T) traversePlan.execute(source, qualifier, traverseExecutor);
  }

  @Override
  public <S, T, Q, C extends Channel1 & MappingTraverse> T mapThruChannel1(S source, Class<C> channelClass, Q qualifier) {
    return mapThruChannel1(source, ChannelFunctions.getChannelId(channelClass), qualifier);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, R> R moveThruChannel0(S source, String cid) {
    TraversePlan traversePlan = traverseAnalyzer.buildMoveObjectHandleThruChannel0Plan(
        ObjectReferenceFunctions.getObjectHandleClass(source.getClass()), cid, ObjectReferenceForms.ObjectHandle);
    return (R) traversePlan.execute(source, traverseExecutor);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, R, Q> R moveThruChannel1(S source, String cid, Q qualifier) {
    TraversePlan traversePlan = traverseAnalyzer.buildMoveObjectHandleThruChannel1Plan(
        ObjectReferenceFunctions.getObjectHandleClass(source.getClass()), cid, ObjectReferenceForms.ObjectHandle);
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
    TraversePlan traversePlan = traverseAnalyzer.buildMapOfMovingObjectHandleThruChannel1Plan(
        ObjectReferenceFunctions.getObjectHandleClass(source.getClass()), cid, ObjectReferenceForms.ObjectHandle);
    return (R) traversePlan.execute(source, qualifier, traverseExecutor);
  }

  @Override
  public <S, T> Mapper0<S, T> autoMapperThruChannel0(Type<S> sourceType, String cid, ObjectReferenceForm targetForm) {
    TraversePlan traversePlan = traverseAnalyzer.buildMapObjectHandleThruChannel0Plan(
        sourceType.asClassType().baseClass(), cid, targetForm
    );
    return new AutoMapper0<>(cid, traversePlan, targetForm, traverseExecutor);
  }

  @Override
  public <S, T, Q> Mapper1<S, T, Q> autoMapperThruChannel1(Type<S> sourceType, String cid, ObjectReferenceForm targetForm) {
    TraversePlan traversePlan = traverseAnalyzer.buildMapObjectHandleThruChannel1Plan(
        sourceType.asClassType().baseClass(), cid, targetForm
    );
    return new AutoMapper1<>(cid, traversePlan, targetForm, traverseExecutor);
  }

  @Override
  public <S, T, Q1, Q2> Mapper2<S, T, Q1, Q2> autoMapperThruChannel2(Type<S> sourceType, String cid, ObjectReferenceForm targetForm) {
    TraversePlan traversePlan = traverseAnalyzer.buildMapObjectHandleThruChannel2Plan(
        sourceType.asClassType().baseClass(), cid, targetForm
    );
    return new AutoMapper2<>(cid, traversePlan, targetForm, traverseExecutor);
  }

  @Override
  public <S, T, Q1, Q2, Q3> Mapper3<S, T, Q1, Q2, Q3> autoMapperThruChannel3(Type<S> sourceType, String cid, ObjectReferenceForm targetForm) {
    TraversePlan traversePlan = traverseAnalyzer.buildMapObjectHandleThruChannel3Plan(
        sourceType.asClassType().baseClass(), cid, targetForm
    );
    return new AutoMapper3<>(cid, traversePlan, targetForm, traverseExecutor);
  }

  @Override
  public <S> Mover0<S> autoMoverThruChannel0(Type<S> sourceType, String cid, ObjectReferenceForm targetForm) {
    TraversePlan traversePlan = traverseAnalyzer.buildMoveObjectHandleThruChannel0Plan(
        sourceType.asClassType().baseClass(), cid, targetForm
    );
    return new AutoMover0<>(cid, traversePlan, targetForm, traverseExecutor);
  }

  @Override
  public <S, Q> Mover1<S, Q> autoMoverThruChannel1(Class<S> sourceClass, String cid, ObjectReferenceForm targetForm) {
    return autoMoverThruChannel1(Types.get(sourceClass), cid, targetForm);
  }

  @Override
  public <S, Q> Mover1<S, Q> autoMoverThruChannel1(Type<S> sourceType, String cid, ObjectReferenceForm targetForm) {
    TraversePlan traversePlan = traverseAnalyzer.buildMoveObjectHandleThruChannel1Plan(
        sourceType.asClassType().baseClass(), cid, targetForm
    );
    return new AutoMover1<>(cid, traversePlan, targetForm, traverseExecutor);
  }

  @Override
  public <S, Q1, Q2> Mover2<S, Q1, Q2> autoMoverThruChannel2(Type<S> sourceType, String cid, ObjectReferenceForm targetForm) {
    TraversePlan traversePlan = traverseAnalyzer.buildMoveObjectHandleThruChannel2Plan(
        sourceType.asClassType().baseClass(), cid, targetForm
    );
    return new AutoMover2<>(cid, traversePlan, targetForm, traverseExecutor);
  }

  @Override
  public <S, Q1, Q2, Q3> Mover3<S, Q1, Q2, Q3> autoMoverThruChannel3(Type<S> sourceType, String cid, ObjectReferenceForm targetForm) {
    TraversePlan traversePlan = traverseAnalyzer.buildMoveObjectHandleThruChannel3Plan(
        sourceType.asClassType().baseClass(), cid, targetForm
    );
    return new AutoMover3<>(cid, traversePlan, targetForm, traverseExecutor);
  }

  @Override
  public <S, T> MapperOfMoving0<S, T> autoMapperOfMovingThruChannel0(Type<S> sourceType, String cid, ObjectReferenceForm targetForm) {
    TraversePlan traversePlan = traverseAnalyzer.buildMapOfMovingObjectHandleThruChannel0Plan(
        sourceType.asClassType().baseClass(), cid, targetForm
    );
    return new AutoMapperOfMoving0<>(cid, traversePlan, targetForm, traverseExecutor);
  }

  @Override
  public <S, T, Q> MapperOfMoving1<S, T, Q> autoMapperOfMovingThruChannel1(Type<S> sourceType, String cid, ObjectReferenceForm targetForm) {
    TraversePlan traversePlan = traverseAnalyzer.buildMapOfMovingObjectHandleThruChannel1Plan(
        sourceType.asClassType().baseClass(), cid, targetForm
    );
    return new AutoMapperOfMoving1<>(cid, traversePlan, targetForm, traverseExecutor);
  }

  @Override
  public <S, T, Q1, Q2> MapperOfMoving2<S, T, Q1, Q2> autoMapperOfMovingThruChannel2(Type<S> sourceType, String cid, ObjectReferenceForm targetForm) {
    TraversePlan traversePlan = traverseAnalyzer.buildMapOfMovingObjectHandleThruChannel2Plan(
        sourceType.asClassType().baseClass(), cid, targetForm
    );
    return new AutoMapperOfMoving2<>(cid, traversePlan, targetForm, traverseExecutor);
  }

  @Override
  public <S, T, Q1, Q2, Q3> MapperOfMoving3<S, T, Q1, Q2, Q3> autoMapperOfMovingThruChannel3(
      Type<S> sourceType, String cid, ObjectReferenceForm targetForm
  ) {
    TraversePlan traversePlan = traverseAnalyzer.buildMapOfMovingObjectHandleThruChannel3Plan(
        sourceType.asClassType().baseClass(), cid, targetForm
    );
    return new AutoMapperOfMoving3<>(cid, traversePlan, targetForm, traverseExecutor);
  }

  @Override
  public <S, T, Q1, Q2, Q3, Q4> MapperOfMoving4<S, T, Q1, Q2, Q3, Q4> autoMapperOfMovingThruChannel4(
      Type<S> sourceType, String cid, ObjectReferenceForm targetForm
  ) {
    TraversePlan traversePlan = traverseAnalyzer.buildMapOfMovingObjectHandleThruChannel4Plan(
        sourceType.asClassType().baseClass(), cid, targetForm
    );
    return new AutoMapperOfMoving4<>(cid, traversePlan, targetForm, traverseExecutor);
  }

  @Override
  public <S, T> Mapper0<S, T> autoMapperThruChannel0(
      Type<S> sourceType, Class<? extends Channel0> channelClass, ObjectReferenceForm targetForm
  ) {
    return autoMapperThruChannel0(sourceType, ChannelFunctions.getChannelId(channelClass), targetForm);
  }

  @Override
  public <S, T, Q> Mapper1<S, T, Q> autoMapperThruChannel1(
      Type<S> sourceType, Class<? extends Channel1> channelClass, ObjectReferenceForm targetForm
  ) {
    return autoMapperThruChannel1(sourceType, ChannelFunctions.getChannelId(channelClass), targetForm);
  }

  @Override
  public <S, T, Q1, Q2> Mapper2<S, T, Q1, Q2> autoMapperThruChannel2(
      Type<S> sourceType, Class<? extends Channel2> channelClass, ObjectReferenceForm targetForm
  ) {
    return autoMapperThruChannel2(sourceType, ChannelFunctions.getChannelId(channelClass), targetForm);
  }

  @Override
  public <S, T, Q1, Q2, Q3> Mapper3<S, T, Q1, Q2, Q3> autoMapperThruChannel3(
      Type<S> sourceType, Class<? extends Channel3> channelClass, ObjectReferenceForm targetForm
  ) {
    return autoMapperThruChannel3(sourceType, ChannelFunctions.getChannelId(channelClass), targetForm);
  }

  @Override
  public <S> Mover0<S> autoMoverThruChannel0(
      Type<S> sourceType, Class<? extends Channel0> channelClass, ObjectReferenceForm targetForm
  ) {
    return autoMoverThruChannel0(sourceType, ChannelFunctions.getChannelId(channelClass), targetForm);
  }

  @Override
  public <S, Q> Mover1<S, Q> autoMoverThruChannel1(
      Type<S> sourceType, Class<? extends Channel1> channelClass, ObjectReferenceForm targetForm
  ) {
    return autoMoverThruChannel1(sourceType, ChannelFunctions.getChannelId(channelClass), targetForm);
  }

  @Override
  public <S, Q1, Q2> Mover2<S, Q1, Q2> autoMoverThruChannel2(
      Type<S> sourceType, Class<? extends Channel2> channelClass, ObjectReferenceForm targetForm
  ) {
    return autoMoverThruChannel2(sourceType, ChannelFunctions.getChannelId(channelClass), targetForm);
  }

  @Override
  public <S, Q1, Q2, Q3> Mover3<S, Q1, Q2, Q3> autoMoverThruChannel3(
      Type<S> sourceType, Class<? extends Channel3> channelClass, ObjectReferenceForm targetForm
  ) {
    return autoMoverThruChannel3(sourceType, ChannelFunctions.getChannelId(channelClass), targetForm);
  }

  @Override
  public <S, T> MapperOfMoving0<S, T> autoMapperOfMovingThruChannel0(
      Type<S> sourceType, Class<? extends Channel0> channelClass, ObjectReferenceForm targetForm
  ) {
    return autoMapperOfMovingThruChannel0(sourceType, ChannelFunctions.getChannelId(channelClass), targetForm);
  }

  @Override
  public <S, T, Q> MapperOfMoving1<S, T, Q> autoMapperOfMovingThruChannel1(
      Type<S> sourceType, Class<? extends Channel1> channelClass, ObjectReferenceForm targetForm
  ) {
    return autoMapperOfMovingThruChannel1(sourceType, ChannelFunctions.getChannelId(channelClass), targetForm);
  }

  @Override
  public <S, T, Q1, Q2> MapperOfMoving2<S, T, Q1, Q2> autoMapperOfMovingThruChannel2(
      Type<S> sourceType, Class<? extends Channel2> channelClass, ObjectReferenceForm targetForm
  ) {
    return autoMapperOfMovingThruChannel2(sourceType, ChannelFunctions.getChannelId(channelClass), targetForm);
  }

  @Override
  public <S, T, Q1, Q2, Q3> MapperOfMoving3<S, T, Q1, Q2, Q3> autoMapperOfMovingThruChannel3(
      Type<S> sourceType, Class<? extends Channel3> channelClass, ObjectReferenceForm targetForm
  ) {
    return autoMapperOfMovingThruChannel3(sourceType, ChannelFunctions.getChannelId(channelClass), targetForm);
  }

  @Override
  public <S, T, Q1, Q2, Q3, Q4> MapperOfMoving4<S, T, Q1, Q2, Q3, Q4> autoMapperOfMovingThruChannel4(
      Type<S> sourceType, Class<? extends Channel4> channelClass, ObjectReferenceForm targetForm
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
  public <T> T getProjection(String name, Class<T> targetObjectHandleClass) {
    return projectionRegistry.getProjection(name, targetObjectHandleClass);
  }

  @Override
  public <T> List<T> getProjections(Class<T> targetObjectHandleClass) {
    return projectionRegistry.getProjections(targetObjectHandleClass);
  }

  @Override
  public <T> void addContextProjection(String name, Class<T> targetObjectHandleClass, T target) {
    projectionRegistry.addContextProjection(name, targetObjectHandleClass, target);
  }

  @Override
  public void removeContextProjection(String name) {
    projectionRegistry.removeContextProjection(name);
  }

  private Unit mainUnitSupplier() {
    return units.stream()
        .filter(tech.intellispaces.reflections.system.Unit::isMain)
        .findFirst()
        .orElseThrow();
  }
}
