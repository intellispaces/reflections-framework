package tech.intellispaces.reflections.framework.system;

import java.util.Collection;
import java.util.List;

import tech.intellispaces.actions.cache.CachedSupplierActions;
import tech.intellispaces.actions.supplier.SupplierAction;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.core.System;
import tech.intellispaces.core.Unit;
import tech.intellispaces.reflections.framework.channel.Channel0;
import tech.intellispaces.reflections.framework.channel.Channel1;
import tech.intellispaces.reflections.framework.channel.Channel2;
import tech.intellispaces.reflections.framework.channel.Channel3;
import tech.intellispaces.reflections.framework.channel.Channel4;
import tech.intellispaces.reflections.framework.engine.Engine;
import tech.intellispaces.reflections.framework.guide.n0.Mapper0;
import tech.intellispaces.reflections.framework.guide.n0.MapperOfMoving0;
import tech.intellispaces.reflections.framework.guide.n0.Mover0;
import tech.intellispaces.reflections.framework.guide.n1.Mapper1;
import tech.intellispaces.reflections.framework.guide.n1.MapperOfMoving1;
import tech.intellispaces.reflections.framework.guide.n1.Mover1;
import tech.intellispaces.reflections.framework.guide.n2.Mapper2;
import tech.intellispaces.reflections.framework.guide.n2.MapperOfMoving2;
import tech.intellispaces.reflections.framework.guide.n2.Mover2;
import tech.intellispaces.reflections.framework.guide.n3.Mapper3;
import tech.intellispaces.reflections.framework.guide.n3.MapperOfMoving3;
import tech.intellispaces.reflections.framework.guide.n3.Mover3;
import tech.intellispaces.reflections.framework.guide.n4.MapperOfMoving4;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.traverse.MappingOfMovingTraverse;
import tech.intellispaces.reflections.framework.traverse.MappingTraverse;

public class ModuleHandleImpl implements ModuleHandle {
    private final Engine engine;
    private final SystemHandle system;
    private final List<UnitHandle> units;
    private final SupplierAction<UnitHandle> mainUnitGetter = CachedSupplierActions.get(this::mainUnitSupplier);

    public ModuleHandleImpl(SystemHandle system, List<UnitHandle> units) {
        this.system = system;
        this.units = units;
        this.engine = system.engine();
    }

    @Override
    public System system() {
        return system;
    }

    @Override
    public Engine engine() {
        return engine;
    }

    @Override
    public UnitHandle mainUnit() {
        return mainUnitGetter.get();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Unit> units() {
        return (List<Unit>) (List<?>) units;
    }

    @Override
    public List<UnitHandle> unitHandles() {
        return units;
    }

    @Override
    public ModuleHandle start() {
        engine.start();
        mainUnit().startupAction().ifPresent(a -> a.castToAction0().execute());
        return this;
    }

    @Override
    public ModuleHandle start(String[] args) {
        engine.start(args);
        mainUnit().startupAction().ifPresent(a -> a.castToAction0().execute());
        return this;
    }

    @Override
    public void stop() {
        mainUnit().shutdownAction().ifPresent(a -> a.castToAction0().execute());
        engine.stop();
    }

    @Override
    public void upload() {
        Modules.unload(this);
    }

    @Override
    public <S, T> T mapThruChannel0(S source, String cid) {
        return engine.mapThruChannel0(source, cid);
    }

    @Override
    public <S, T, C extends Channel0 & MappingTraverse> T mapThruChannel0(S source, Class<C> channelClass) {
        return engine.mapThruChannel0(source, channelClass);
    }

    @Override
    public <S, T, Q> T mapThruChannel1(S source, String cid, Q qualifier) {
        return engine.mapThruChannel1(source, cid, qualifier);
    }

    @Override
    public <S, T, Q, C extends Channel1 & MappingTraverse> T mapThruChannel1(S source, Class<C> channelClass, Q qualifier) {
        return engine.mapThruChannel1(source, channelClass, qualifier);
    }

    @Override
    public <S, R> R moveThruChannel0(S source, String cid) {
        return engine.moveThruChannel0(source, cid);
    }

    @Override
    public <S, R, Q> R moveThruChannel1(S source, String cid, Q qualifier) {
        return engine.moveThruChannel1(source, cid, qualifier);
    }

    @Override
    public <S, R, Q, C extends Channel1 & MappingOfMovingTraverse> R mapOfMovingThruChannel1(
        S source, Class<C> channelClass, Q qualifier
    ) {
        return engine.mapOfMovingThruChannel1(source, channelClass, qualifier);
    }

    @Override
    public <S, R, Q> R mapOfMovingThruChannel1(S source, String cid, Q qualifier) {
        return engine.mapOfMovingThruChannel1(source, cid, qualifier);
    }

    @Override
    public <S, T> Mapper0<S, T> autoMapperThruChannel0(Type<S> sourceType, String cid, ReflectionForm targetForm) {
        return engine.autoMapperThruChannel0(sourceType, cid, targetForm);
    }

    @Override
    public <S, T, Q> Mapper1<S, T, Q> autoMapperThruChannel1(
        Type<S> sourceType, String cid, ReflectionForm targetForm
    ) {
        return engine.autoMapperThruChannel1(sourceType, cid, targetForm);
    }

    @Override
    public <S, T, Q1, Q2> Mapper2<S, T, Q1, Q2> autoMapperThruChannel2(
        Type<S> sourceType, String cid, ReflectionForm targetForm
    ) {
        return engine.autoMapperThruChannel2(sourceType, cid, targetForm);
    }

    @Override
    public <S, T, Q1, Q2, Q3> Mapper3<S, T, Q1, Q2, Q3> autoMapperThruChannel3(
        Type<S> sourceType, String cid, ReflectionForm targetForm
    ) {
        return engine.autoMapperThruChannel3(sourceType, cid, targetForm);
    }

    @Override
    public <S> Mover0<S> autoMoverThruChannel0(Type<S> sourceType, String cid, ReflectionForm targetForm) {
        return engine.autoMoverThruChannel0(sourceType, cid, targetForm);
    }

    @Override
    public <S, Q> Mover1<S, Q> autoMoverThruChannel1(Class<S> sourceClass, String cid, ReflectionForm targetForm) {
        return engine.autoMoverThruChannel1(sourceClass, cid, targetForm);
    }

    @Override
    public <S, Q> Mover1<S, Q> autoMoverThruChannel1(Type<S> sourceType, String cid, ReflectionForm targetForm) {
        return engine.autoMoverThruChannel1(sourceType, cid, targetForm);
    }

    @Override
    public <S, Q1, Q2> Mover2<S, Q1, Q2> autoMoverThruChannel2(
        Type<S> sourceType, String cid, ReflectionForm targetForm
    ) {
        return engine.autoMoverThruChannel2(sourceType, cid, targetForm);
    }

    @Override
    public <S, Q1, Q2, Q3> Mover3<S, Q1, Q2, Q3> autoMoverThruChannel3(
        Type<S> sourceType, String cid, ReflectionForm targetForm
    ) {
        return engine.autoMoverThruChannel3(sourceType, cid, targetForm);
    }

    @Override
    public <S, T> MapperOfMoving0<S, T> autoMapperOfMovingThruChannel0(
        Type<S> sourceType, String cid, ReflectionForm targetForm
    ) {
        return engine.autoMapperOfMovingThruChannel0(sourceType, cid, targetForm);
    }

    @Override
    public <S, T, Q> MapperOfMoving1<S, T, Q> autoMapperOfMovingThruChannel1(
        Type<S> sourceType, String cid, ReflectionForm targetForm
    ) {
        return engine.autoMapperOfMovingThruChannel1(sourceType, cid, targetForm);
    }

    @Override
    public <S, T, Q1, Q2> MapperOfMoving2<S, T, Q1, Q2> autoMapperOfMovingThruChannel2(
        Type<S> sourceType, String cid, ReflectionForm targetForm
    ) {
        return engine.autoMapperOfMovingThruChannel2(sourceType, cid, targetForm);
    }

    @Override
    public <S, T, Q1, Q2, Q3> MapperOfMoving3<S, T, Q1, Q2, Q3> autoMapperOfMovingThruChannel3(
        Type<S> sourceType, String cid, ReflectionForm targetForm
    ) {
        return engine.autoMapperOfMovingThruChannel3(sourceType, cid, targetForm);
    }

    @Override
    public <S, T, Q1, Q2, Q3, Q4> MapperOfMoving4<S, T, Q1, Q2, Q3, Q4> autoMapperOfMovingThruChannel4(
        Type<S> sourceType, String cid, ReflectionForm targetForm
    ) {
        return engine.autoMapperOfMovingThruChannel4(sourceType, cid, targetForm);
    }

    @Override
    public <S, T> Mapper0<S, T> autoMapperThruChannel0(
        Type<S> sourceType, Class<? extends Channel0> channelClass, ReflectionForm targetForm
    ) {
        return engine.autoMapperThruChannel0(sourceType, channelClass, targetForm);
    }

    @Override
    public <S, T, Q> Mapper1<S, T, Q> autoMapperThruChannel1(
        Type<S> sourceType, Class<? extends Channel1> channelClass, ReflectionForm targetForm
    ) {
        return engine.autoMapperThruChannel1(sourceType, channelClass, targetForm);
    }

    @Override
    public <S, T, Q1, Q2> Mapper2<S, T, Q1, Q2> autoMapperThruChannel2(
        Type<S> sourceType, Class<? extends Channel2> channelClass, ReflectionForm targetForm
    ) {
        return engine.autoMapperThruChannel2(sourceType, channelClass, targetForm);
    }

    @Override
    public <S, T, Q1, Q2, Q3> Mapper3<S, T, Q1, Q2, Q3> autoMapperThruChannel3(
        Type<S> sourceType, Class<? extends Channel3> channelClass, ReflectionForm targetForm
    ) {
        return engine.autoMapperThruChannel3(sourceType, channelClass, targetForm);
    }

    @Override
    public <S> Mover0<S> autoMoverThruChannel0(
        Type<S> sourceType, Class<? extends Channel0> channelClass, ReflectionForm targetForm
    ) {
        return engine.autoMoverThruChannel0(sourceType, channelClass, targetForm);
    }

    @Override
    public <S, Q> Mover1<S, Q> autoMoverThruChannel1(
        Type<S> sourceType, Class<? extends Channel1> channelClass, ReflectionForm targetForm
    ) {
        return engine.autoMoverThruChannel1(sourceType, channelClass, targetForm);
    }

    @Override
    public <S, Q1, Q2> Mover2<S, Q1, Q2> autoMoverThruChannel2(
        Type<S> sourceType, Class<? extends Channel2> channelClass, ReflectionForm targetForm
    ) {
        return engine.autoMoverThruChannel2(sourceType, channelClass, targetForm);
    }

    @Override
    public <S, Q1, Q2, Q3> Mover3<S, Q1, Q2, Q3> autoMoverThruChannel3(
        Type<S> sourceType, Class<? extends Channel3> channelClass, ReflectionForm targetForm
    ) {
        return engine.autoMoverThruChannel3(sourceType, channelClass, targetForm);
    }

    @Override
    public <S, T> MapperOfMoving0<S, T> autoMapperOfMovingThruChannel0(
        Type<S> sourceType, Class<? extends Channel0> channelClass, ReflectionForm targetForm
    ) {
        return engine.autoMapperOfMovingThruChannel0(sourceType, channelClass, targetForm);
    }

    @Override
    public <S, T, Q> MapperOfMoving1<S, T, Q> autoMapperOfMovingThruChannel1(
        Type<S> sourceType, Class<? extends Channel1> channelClass, ReflectionForm targetForm
    ) {
        return engine.autoMapperOfMovingThruChannel1(sourceType, channelClass, targetForm);
    }

    @Override
    public <S, T, Q1, Q2> MapperOfMoving2<S, T, Q1, Q2> autoMapperOfMovingThruChannel2(
        Type<S> sourceType, Class<? extends Channel2> channelClass, ReflectionForm targetForm
    ) {
        return engine.autoMapperOfMovingThruChannel2(sourceType, channelClass, targetForm);
    }

    @Override
    public <S, T, Q1, Q2, Q3> MapperOfMoving3<S, T, Q1, Q2, Q3> autoMapperOfMovingThruChannel3(
        Type<S> sourceType, Class<? extends Channel3> channelClass, ReflectionForm targetForm
    ) {
        return engine.autoMapperOfMovingThruChannel3(sourceType, channelClass, targetForm);
    }

    @Override
    public <S, T, Q1, Q2, Q3, Q4> MapperOfMoving4<S, T, Q1, Q2, Q3, Q4> autoMapperOfMovingThruChannel4(
        Type<S> sourceType, Class<? extends Channel4> channelClass, ReflectionForm targetForm
    ) {
        return engine.autoMapperOfMovingThruChannel4(sourceType, channelClass, targetForm);
    }

    @Override
    public <G> G getAutoGuide(Class<G> guideClass) {
        return engine.getAutoGuide(guideClass);
    }

    @Override
    public <T> T getProjection(String name, Class<T> targetReflectionClass) {
        return engine.getProjection(name, targetReflectionClass);
    }

    @Override
    public <T> List<T> getProjections(Class<T> targetReflectionClass) {
        return engine.getProjections(targetReflectionClass);
    }

    @Override
    public Collection<ModuleProjection> moduleProjections() {
        return engine.moduleProjections();
    }

    @Override
    public <T> void addContextProjection(String name, Class<T> targetReflectionClass, T target) {
        engine.addContextProjection(name, targetReflectionClass, target);
    }

    @Override
    public void removeContextProjection(String name) {
        engine.removeContextProjection(name);
    }

    private UnitHandle mainUnitSupplier() {
        return units.stream()
            .filter(Unit::isMain)
            .findFirst()
            .orElseThrow();
    }
}
