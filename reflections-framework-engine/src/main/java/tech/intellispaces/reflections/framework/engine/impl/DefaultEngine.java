package tech.intellispaces.reflections.framework.engine.impl;

import java.util.Collection;
import java.util.List;

import tech.intellispaces.actions.Action0;
import tech.intellispaces.actions.Action1;
import tech.intellispaces.actions.Action10;
import tech.intellispaces.actions.Action2;
import tech.intellispaces.actions.Action3;
import tech.intellispaces.actions.Action4;
import tech.intellispaces.actions.Action5;
import tech.intellispaces.actions.Action6;
import tech.intellispaces.actions.Action7;
import tech.intellispaces.actions.Action8;
import tech.intellispaces.actions.Action9;
import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.commons.type.Types;
import tech.intellispaces.core.Domain;
import tech.intellispaces.core.Reflection;
import tech.intellispaces.core.ReflectionContract;
import tech.intellispaces.core.Rid;
import tech.intellispaces.reflections.framework.channel.Channel0;
import tech.intellispaces.reflections.framework.channel.Channel1;
import tech.intellispaces.reflections.framework.channel.Channel2;
import tech.intellispaces.reflections.framework.channel.Channel3;
import tech.intellispaces.reflections.framework.channel.Channel4;
import tech.intellispaces.reflections.framework.engine.Engine;
import tech.intellispaces.reflections.framework.guide.Guide;
import tech.intellispaces.reflections.framework.guide.GuideFunctions;
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
import tech.intellispaces.reflections.framework.reflection.ReflectionHandle;
import tech.intellispaces.reflections.framework.reflection.ReflectionHandleImpl;
import tech.intellispaces.reflections.framework.reflection.ReflectionRealizationType;
import tech.intellispaces.reflections.framework.reflection.ReflectionRealizationTypeImpl;
import tech.intellispaces.reflections.framework.space.channel.ChannelFunctions;
import tech.intellispaces.reflections.framework.system.AutoGuideRegistry;
import tech.intellispaces.reflections.framework.system.FactoryRegistry;
import tech.intellispaces.reflections.framework.system.GuideRegistry;
import tech.intellispaces.reflections.framework.system.ModuleProjection;
import tech.intellispaces.reflections.framework.system.ProjectionDefinition;
import tech.intellispaces.reflections.framework.system.ProjectionRegistry;
import tech.intellispaces.reflections.framework.system.ReflectionRegistry;
import tech.intellispaces.reflections.framework.system.TraverseAnalyzer;
import tech.intellispaces.reflections.framework.system.TraverseExecutor;
import tech.intellispaces.reflections.framework.traverse.MappingOfMovingTraverse;
import tech.intellispaces.reflections.framework.traverse.MappingTraverse;
import tech.intellispaces.reflections.framework.traverse.plan.DeclarativeTraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.TraversePlan;

public class DefaultEngine implements Engine {
  private final ProjectionRegistry projectionRegistry;
  private final GuideRegistry guideRegistry;
  private final AutoGuideRegistry autoGuideRegistry;
  private final TraverseAnalyzer traverseAnalyzer;
  private final TraverseExecutor traverseExecutor;
  private final FactoryRegistry factoryRegistry;
  private final ReflectionRegistry reflectionRegistry;

  public DefaultEngine(
      ProjectionRegistry projectionRegistry,
      GuideRegistry guideRegistry,
      AutoGuideRegistry autoGuideRegistry,
      TraverseAnalyzer traverseAnalyzer,
      TraverseExecutor traverseExecutor,
      FactoryRegistry factoryRegistry,
      ReflectionRegistry reflectionRegistry
  ) {
    this.projectionRegistry = projectionRegistry;
    this.guideRegistry = guideRegistry;
    this.autoGuideRegistry = autoGuideRegistry;
    this.traverseAnalyzer = traverseAnalyzer;
    this.traverseExecutor = traverseExecutor;
    this.factoryRegistry = factoryRegistry;
    this.reflectionRegistry = reflectionRegistry;
  }

  @Override
  public void start() {
    start(new String[] {});
  }

  @Override
  public void start(String[] args) {
    loadProjections();
  }

  private void loadProjections() {
    projectionRegistry.onStartup();
  }

  @Override
  public void stop() {

  }

  @Override
  public <S, T> T mapSourceTo(S source, Domain domain) {
    throw NotImplementedExceptions.withCode("ZIKQyQ");
  }

  @Override
  public Reflection mapSourceTo(Reflection source, Domain domain) {
    throw NotImplementedExceptions.withCode("Vfn2cg");
  }

  @Override
  @SuppressWarnings("unchecked")
  public <R extends Reflection> R mapSourceTo(Reflection source, Domain targetDomain, Class<R> targetClass) {
    DeclarativeTraversePlan traversePlan = traverseAnalyzer.buildMapToTraversePlan(source, targetDomain, targetClass);
    return (R) traversePlan.execute(traverseExecutor);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, T> T mapThruChannel0(S source, Rid cid) {
    DeclarativeTraversePlan traversePlan = traverseAnalyzer.buildMapThruChannel0TraversePlan(
        ReflectionFunctions.getReflectionClass(source.getClass()), cid, ReflectionForms.Reflection);
    return (T) traversePlan.execute(source, traverseExecutor);
  }

  @Override
  public <S, T, C extends Channel0 & MappingTraverse> T mapThruChannel0(S source, Class<C> channelClass) {
    return mapThruChannel0(source, ChannelFunctions.getChannelId(channelClass));
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, T, Q> T mapThruChannel1(S source, Rid cid, Q qualifier) {
    DeclarativeTraversePlan traversePlan = traverseAnalyzer.buildMapThruChannel1TraversePlan(
        ReflectionFunctions.getReflectionClass(source.getClass()), cid, ReflectionForms.Reflection);
    return (T) traversePlan.execute(source, qualifier, traverseExecutor);
  }

  @Override
  public <S, T, Q, C extends Channel1 & MappingTraverse> T mapThruChannel1(S source, Class<C> channelClass, Q qualifier) {
    return mapThruChannel1(source, ChannelFunctions.getChannelId(channelClass), qualifier);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, R> R moveThruChannel0(S source, Rid cid) {
    TraversePlan traversePlan = traverseAnalyzer.buildMoveThruChannel0TraversePlan(
        ReflectionFunctions.getReflectionClass(source.getClass()), cid, ReflectionForms.Reflection);
    return (R) traversePlan.execute(source, traverseExecutor);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, R, Q> R moveThruChannel1(S source, Rid cid, Q qualifier) {
    TraversePlan traversePlan = traverseAnalyzer.buildMoveThruChannel1TraversePlan(
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
  public <S, R, Q> R mapOfMovingThruChannel1(S source, Rid cid, Q qualifier) {
    TraversePlan traversePlan = traverseAnalyzer.buildMapOfMovingThruChannel1TraversePlan(
        ReflectionFunctions.getReflectionClass(source.getClass()), cid, ReflectionForms.Reflection);
    return (R) traversePlan.execute(source, qualifier, traverseExecutor);
  }

  @Override
  public void addProjection(ProjectionDefinition projectionDefinition) {
    projectionRegistry.addProjection(projectionDefinition);
  }

  @Override
  public <T> List<T> findProjections(Class<T> targetReflectionClass) {
    return projectionRegistry.findProjections(targetReflectionClass);
  }

  @Override
  public void addGuide(Guide<?, ?> guide) {
    guideRegistry.addGuide(guide);
  }

  @Override
  public Reflection createReflection(ReflectionContract contract) {
    Reflection reflection = factoryRegistry.factoryAction(
        contract.domain(),
        contract.type()
    ).execute(contract.properties());
    return reflectionRegistry.register(reflection);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, T> Mapper0<S, T> autoMapperThruChannel0(
      Type<S> sourceType, Rid cid, ReflectionForm targetForm
  ) {
    TraversePlan traversePlan = traverseAnalyzer.buildMapThruChannel0TraversePlan(
        sourceType.asClassType().baseClass(), cid, targetForm
    );
    return new AutoMapper0<>(
        cid,
        traversePlan,
        (Class<S>) sourceType.asClassType().baseClass(),
        targetForm,
        traverseExecutor
    );
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, T, Q> Mapper1<S, T, Q> autoMapperThruChannel1(
      Type<S> sourceType, Rid cid, ReflectionForm targetForm
  ) {
    TraversePlan traversePlan = traverseAnalyzer.buildMapThruChannel1TraversePlan(
        sourceType.asClassType().baseClass(), cid, targetForm
    );
    return new AutoMapper1<>(
        cid,
        traversePlan,
        (Class<S>) sourceType.asClassType().baseClass(),
        targetForm,
        traverseExecutor
    );
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, T, Q1, Q2> Mapper2<S, T, Q1, Q2> autoMapperThruChannel2(
      Type<S> sourceType, Rid cid, ReflectionForm targetForm
  ) {
    TraversePlan traversePlan = traverseAnalyzer.buildMapThruChannel2TraversePlan(
        sourceType.asClassType().baseClass(), cid, targetForm
    );
    return new AutoMapper2<>(
        cid,
        traversePlan,
        (Class<S>) sourceType.asClassType().baseClass(),
        targetForm,
        traverseExecutor
    );
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, T, Q1, Q2, Q3> Mapper3<S, T, Q1, Q2, Q3> autoMapperThruChannel3(
      Type<S> sourceType, Rid cid, ReflectionForm targetForm
  ) {
    TraversePlan traversePlan = traverseAnalyzer.buildMapThruChannel3TraversePlan(
        sourceType.asClassType().baseClass(), cid, targetForm
    );
    return new AutoMapper3<>(
        cid,
        traversePlan,
        (Class<S>) sourceType.asClassType().baseClass(),
        targetForm,
        traverseExecutor
    );
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S> Mover0<S> autoMoverThruChannel0(Type<S> sourceType, Rid cid, ReflectionForm targetForm) {
    TraversePlan traversePlan = traverseAnalyzer.buildMoveThruChannel0TraversePlan(
        sourceType.asClassType().baseClass(), cid, targetForm
    );
    return new AutoMover0<>(
        cid,
        traversePlan,
        (Class<S>) sourceType.asClassType().baseClass(),
        targetForm,
        traverseExecutor
    );
  }

  @Override
  public <S, Q> Mover1<S, Q> autoMoverThruChannel1(
      Class<S> sourceClass, Rid cid, ReflectionForm targetForm
  ) {
    return autoMoverThruChannel1(Types.get(sourceClass), cid, targetForm);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, Q> Mover1<S, Q> autoMoverThruChannel1(
      Type<S> sourceType, Rid cid, ReflectionForm targetForm
  ) {
    TraversePlan traversePlan = traverseAnalyzer.buildMoveThruChannel1TraversePlan(
        sourceType.asClassType().baseClass(), cid, targetForm
    );
    return new AutoMover1<>(
        cid,
        traversePlan,
        (Class<S>) sourceType.asClassType().baseClass(),
        targetForm,
        traverseExecutor
    );
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, Q1, Q2> Mover2<S, Q1, Q2> autoMoverThruChannel2(
      Type<S> sourceType, Rid cid, ReflectionForm targetForm
  ) {
    TraversePlan traversePlan = traverseAnalyzer.buildMoveThruChannel2TraversePlan(
        sourceType.asClassType().baseClass(), cid, targetForm
    );
    return new AutoMover2<>(
        cid,
        traversePlan,
        (Class<S>) sourceType.asClassType().baseClass(),
        targetForm,
        traverseExecutor
    );
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, Q1, Q2, Q3> Mover3<S, Q1, Q2, Q3> autoMoverThruChannel3(
      Type<S> sourceType, Rid cid, ReflectionForm targetForm
  ) {
    TraversePlan traversePlan = traverseAnalyzer.buildMoveThruChannel3TraversePlan(
        sourceType.asClassType().baseClass(), cid, targetForm
    );
    return new AutoMover3<>(
        cid,
        traversePlan,
        (Class<S>) sourceType.asClassType().baseClass(),
        targetForm,
        traverseExecutor
    );
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, T> MapperOfMoving0<S, T> autoMapperOfMovingThruChannel0(
      Type<S> sourceType, Rid cid, ReflectionForm targetForm
  ) {
    TraversePlan traversePlan = traverseAnalyzer.buildMapOfMovingThruChannel0TraversePlan(
        sourceType.asClassType().baseClass(), cid, targetForm
    );
    return new AutoMapperOfMoving0<>(
        cid,
        traversePlan,
        (Class<S>) sourceType.asClassType().baseClass(),
        targetForm,
        traverseExecutor
    );
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, T, Q> MapperOfMoving1<S, T, Q> autoMapperOfMovingThruChannel1(
      Type<S> sourceType, Rid cid, ReflectionForm targetForm
  ) {
    TraversePlan traversePlan = traverseAnalyzer.buildMapOfMovingThruChannel1TraversePlan(
        sourceType.asClassType().baseClass(), cid, targetForm
    );
    return new AutoMapperOfMoving1<>(
        cid,
        traversePlan,
        (Class<S>) sourceType.asClassType().baseClass(),
        targetForm,
        traverseExecutor
    );
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, T, Q1, Q2> MapperOfMoving2<S, T, Q1, Q2> autoMapperOfMovingThruChannel2(
      Type<S> sourceType, Rid cid, ReflectionForm targetForm
  ) {
    TraversePlan traversePlan = traverseAnalyzer.buildMapOfMovingThruChannel2TraversePlan(
        sourceType.asClassType().baseClass(), cid, targetForm
    );
    return new AutoMapperOfMoving2<>(
        cid,
        traversePlan,
        (Class<S>) sourceType.asClassType().baseClass(),
        targetForm,
        traverseExecutor
    );
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, T, Q1, Q2, Q3> MapperOfMoving3<S, T, Q1, Q2, Q3> autoMapperOfMovingThruChannel3(
      Type<S> sourceType, Rid cid, ReflectionForm targetForm
  ) {
    TraversePlan traversePlan = traverseAnalyzer.buildMapOfMovingThruChannel3TraversePlan(
        sourceType.asClassType().baseClass(), cid, targetForm
    );
    return new AutoMapperOfMoving3<>(
        cid,
        traversePlan,
        (Class<S>) sourceType.asClassType().baseClass(),
        targetForm,
        traverseExecutor
    );
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, T, Q1, Q2, Q3, Q4> MapperOfMoving4<S, T, Q1, Q2, Q3, Q4> autoMapperOfMovingThruChannel4(
      Type<S> sourceType, Rid cid, ReflectionForm targetForm
  ) {
    TraversePlan traversePlan = traverseAnalyzer.buildMapOfMovingThruChannel4TraversePlan(
        sourceType.asClassType().baseClass(), cid, targetForm
    );
    return new AutoMapperOfMoving4<>(
        cid,
        traversePlan,
        (Class<S>) sourceType.asClassType().baseClass(),
        targetForm,
        traverseExecutor
    );
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
  public List<tech.intellispaces.core.Guide> guides() {
    return projectionRegistry.findProjections(tech.intellispaces.core.Guide.class);
  }

  @Override
  public <G> List<G> guides(Class<G> guideClass) {
    return projectionRegistry.findProjections(guideClass);
  }

  @Override
  public <G> G getAutoGuide(Class<G> guideClass) {
    return autoGuideRegistry.getGuide(guideClass);
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
  public Collection<ModuleProjection> moduleProjections() {
    return projectionRegistry.moduleProjections();
  }

  @Override
  public <T> void addContextProjection(String name, Class<T> targetReflectionClass, T target) {
    projectionRegistry.addContextProjection(name, targetReflectionClass, target);
  }

  @Override
  public void removeContextProjection(String name) {
    projectionRegistry.removeContextProjection(name);
  }

  @Override
  public void registerReflectionRealizationType(ReflectionRealizationType type) {
    loadReflectionGuides(type.realizationClass());
  }

  private void loadReflectionGuides(Class<?> reflectionClass) {
    Class<?> actualReflectionClass = ReflectionFunctions.getReflectionClass(reflectionClass);
    if (actualReflectionClass != null) {
      List<Guide<?, ?>> reflectionGuides = GuideFunctions.loadReflectionsGuides(actualReflectionClass);
      reflectionGuides.forEach(guideRegistry::addGuide);
    }
  }

  @Override
  public <W> ReflectionHandle registerReflection(W reflection, ReflectionRealizationType type) {
    var typeImpl = (ReflectionRealizationTypeImpl) type;
    return new ReflectionHandleImpl(
        type,
        typeImpl.methodActions(),
        typeImpl.guideActions(),
        typeImpl.injections()
    );
  }

  @Override
  public <R> Action0<R> getFactoryAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<R> targetReflectionType
  ) {
    return factoryRegistry.getFactoryAction(targetDomainClass, contractType, targetReflectionType);
  }

  @Override
  public <R, Q> Action1<R, Q> getFactoryAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q> contractQualifierType,
      Type<R> targetReflectionType
  ) {
    return factoryRegistry.getFactoryAction(
        targetDomainClass, contractType, contractQualifierType, targetReflectionType
    );
  }

  @Override
  public <R, Q1, Q2> Action2<R, Q1, Q2> getFactoryAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<R> targetReflectionType
  ) {
    return factoryRegistry.getFactoryAction(
        targetDomainClass,
        contractType,
        contractQualifierType1,
        contractQualifierType2,
        targetReflectionType
    );
  }

  @Override
  public <R, Q1, Q2, Q3> Action3<R, Q1, Q2, Q3> getFactoryAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<R> targetReflectionType
  ) {
    return factoryRegistry.getFactoryAction(
        targetDomainClass,
        contractType,
        contractQualifierType1,
        contractQualifierType2,
        contractQualifierType3,
        targetReflectionType
    );
  }

  @Override
  public <R, Q1, Q2, Q3, Q4> Action4<R, Q1, Q2, Q3, Q4> getFactoryAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<Q4> contractQualifierType4,
      Type<R> targetReflectionType
  ) {
    return factoryRegistry.getFactoryAction(
        targetDomainClass,
        contractType,
        contractQualifierType1,
        contractQualifierType2,
        contractQualifierType3,
        contractQualifierType4,
        targetReflectionType
    );
  }

  @Override
  public <R, Q1, Q2, Q3, Q4, Q5> Action5<R, Q1, Q2, Q3, Q4, Q5> getFactoryAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<Q4> contractQualifierType4,
      Type<Q5> contractQualifierType5,
      Type<R> targetReflectionType
  ) {
    return factoryRegistry.getFactoryAction(
        targetDomainClass,
        contractType,
        contractQualifierType1,
        contractQualifierType2,
        contractQualifierType3,
        contractQualifierType4,
        contractQualifierType5,
        targetReflectionType
    );
  }

  @Override
  public <R, Q1, Q2, Q3, Q4, Q5, Q6> Action6<R, Q1, Q2, Q3, Q4, Q5, Q6> getFactoryAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<Q4> contractQualifierType4,
      Type<Q5> contractQualifierType5,
      Type<Q6> contractQualifierType6,
      Type<R> targetReflectionType
  ) {
    return factoryRegistry.getFactoryAction(
        targetDomainClass,
        contractType,
        contractQualifierType1,
        contractQualifierType2,
        contractQualifierType3,
        contractQualifierType4,
        contractQualifierType5,
        contractQualifierType6,
        targetReflectionType
    );
  }

  @Override
  public <R, Q1, Q2, Q3, Q4, Q5, Q6, Q7> Action7<R, Q1, Q2, Q3, Q4, Q5, Q6, Q7> getFactoryAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<Q4> contractQualifierType4,
      Type<Q5> contractQualifierType5,
      Type<Q6> contractQualifierType6,
      Type<Q7> contractQualifierType7,
      Type<R> targetReflectionType
  ) {
    return factoryRegistry.getFactoryAction(
        targetDomainClass,
        contractType,
        contractQualifierType1,
        contractQualifierType2,
        contractQualifierType3,
        contractQualifierType4,
        contractQualifierType5,
        contractQualifierType6,
        contractQualifierType7,
        targetReflectionType
    );
  }

  @Override
  public <R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8> Action8<R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8> getFactoryAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<Q4> contractQualifierType4,
      Type<Q5> contractQualifierType5,
      Type<Q6> contractQualifierType6,
      Type<Q7> contractQualifierType7,
      Type<Q8> contractQualifierType8,
      Type<R> targetReflectionType
  ) {
    return factoryRegistry.getFactoryAction(
        targetDomainClass,
        contractType,
        contractQualifierType1,
        contractQualifierType2,
        contractQualifierType3,
        contractQualifierType4,
        contractQualifierType5,
        contractQualifierType6,
        contractQualifierType7,
        contractQualifierType8,
        targetReflectionType
    );
  }

  @Override
  public <R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9> Action9<R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9> getFactoryAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<Q4> contractQualifierType4,
      Type<Q5> contractQualifierType5,
      Type<Q6> contractQualifierType6,
      Type<Q7> contractQualifierType7,
      Type<Q8> contractQualifierType8,
      Type<Q9> contractQualifierType9,
      Type<R> targetReflectionType
  ) {
    return factoryRegistry.getFactoryAction(
        targetDomainClass,
        contractType,
        contractQualifierType1,
        contractQualifierType2,
        contractQualifierType3,
        contractQualifierType4,
        contractQualifierType5,
        contractQualifierType6,
        contractQualifierType7,
        contractQualifierType8,
        contractQualifierType9,
        targetReflectionType
    );
  }

  @Override
  public <R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10> Action10<R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10> getFactoryAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<Q4> contractQualifierType4,
      Type<Q5> contractQualifierType5,
      Type<Q6> contractQualifierType6,
      Type<Q7> contractQualifierType7,
      Type<Q8> contractQualifierType8,
      Type<Q9> contractQualifierType9,
      Type<Q10> contractQualifierType10,
      Type<R> targetReflectionType
  ) {
    return factoryRegistry.getFactoryAction(
        targetDomainClass,
        contractType,
        contractQualifierType1,
        contractQualifierType2,
        contractQualifierType3,
        contractQualifierType4,
        contractQualifierType5,
        contractQualifierType6,
        contractQualifierType7,
        contractQualifierType8,
        contractQualifierType9,
        contractQualifierType10,
        targetReflectionType
    );

  }
}
