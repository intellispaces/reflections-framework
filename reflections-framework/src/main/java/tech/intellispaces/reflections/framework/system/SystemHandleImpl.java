package tech.intellispaces.reflections.framework.system;

import java.util.List;

import tech.intellispaces.core.Reflection;
import tech.intellispaces.core.ReflectionChannel;
import tech.intellispaces.core.ReflectionContract;
import tech.intellispaces.core.ReflectionDomain;
import tech.intellispaces.core.ReflectionPoint;
import tech.intellispaces.core.ReflectionSpace;
import tech.intellispaces.core.Rid;
import tech.intellispaces.core.TraversableReflection;
import tech.intellispaces.core.TraversableReflectionChannel;
import tech.intellispaces.core.TraversableReflectionDomain;
import tech.intellispaces.core.TraversableReflectionPoint;
import tech.intellispaces.core.TraversableReflectionSpace;
import tech.intellispaces.reflections.framework.channel.Channel0;
import tech.intellispaces.reflections.framework.channel.Channel1;
import tech.intellispaces.reflections.framework.engine.Engine;
import tech.intellispaces.reflections.framework.traverse.MappingOfMovingTraverse;
import tech.intellispaces.reflections.framework.traverse.MappingTraverse;

public class SystemHandleImpl implements SystemHandle {
    private final Engine engine;
    private ModuleHandle currentModule;

    public SystemHandleImpl(Engine engine) {
        this.engine = engine;
    }

    void setCurrentModule(ModuleHandle currentModule) {
        this.currentModule = currentModule;
    }

    @Override
    public ModuleHandle currentModule() {
        return currentModule;
    }

    @Override
    public Engine engine() {
        return engine;
    }

    @Override
    public TraversableReflection getReflection(Reflection reflection) {
        return engine.getReflection(reflection);
    }

    @Override
    public TraversableReflectionPoint getReflection(ReflectionPoint point) {
        return engine.getReflection(point);
    }

    @Override
    public TraversableReflectionDomain getReflection(ReflectionDomain domain) {
        return engine.getReflection(domain);
    }

    @Override
    public TraversableReflectionChannel getReflection(ReflectionChannel channel) {
        return engine.getReflection(channel);
    }

    @Override
    public TraversableReflectionSpace getReflection(ReflectionSpace space) {
        return engine.getReflection(space);
    }

    @Override
    public <T> T castToReflectionPoint(Reflection reflection, Class<T> reflectionClass) {
        return engine.castToReflectionPoint(reflection, reflectionClass);
    }

    @Override
    public TraversableReflectionPoint createReflection(ReflectionContract contract) {
        return engine.createReflection(contract);
    }

    @Override
    public List<ReflectionFactory> findFactories(ReflectionDomain domain) {
        return engine.findFactories(domain);
    }

    @Override
    public <S, T> T mapSourceTo(S source, ReflectionDomain domain) {
        return engine.mapSourceTo(source, domain);
    }

    @Override
    public TraversableReflectionPoint mapSourceTo(ReflectionPoint source, ReflectionDomain domain) {
        return engine.mapSourceTo(source, domain);
    }

    @Override
    public <R extends Reflection> R mapSourceTo(ReflectionPoint source, ReflectionDomain domain, Class<R> targetClass) {
        return engine.mapSourceTo(source, domain, targetClass);
    }

    @Override
    public <S, T> T mapThruChannel0(S source, Rid cid) {
        return null;
    }

    @Override
    public <S, T, C extends Channel0 & MappingTraverse> T mapThruChannel0(S source, Class<C> channelClass) {
        return null;
    }

    @Override
    public <S, T, Q> T mapThruChannel1(S source, Rid cid, Q qualifier) {
        return null;
    }

    @Override
    public <S, T, Q, C extends Channel1 & MappingTraverse> T mapThruChannel1(
        S source, Class<C> channelClass, Q qualifier
    ) {
        return null;
    }

    @Override
    public <S, R> R moveThruChannel0(S source, Rid cid) {
        return null;
    }

    @Override
    public <S, R, Q> R moveThruChannel1(S source, Rid cid, Q qualifier) {
        return null;
    }

    @Override
    public <S, R, Q, C extends Channel1 & MappingOfMovingTraverse> R mapOfMovingThruChannel1(
        S source, Class<C> channelClass, Q qualifier
    ) {
        return null;
    }

    @Override
    public <S, R, Q> R mapOfMovingThruChannel1(S source, Rid cid, Q qualifier) {
        return null;
    }
}
