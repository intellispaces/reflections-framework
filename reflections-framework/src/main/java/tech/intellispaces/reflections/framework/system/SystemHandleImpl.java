package tech.intellispaces.reflections.framework.system;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import tech.intellispaces.core.Reflection;
import tech.intellispaces.core.ReflectionContract;
import tech.intellispaces.core.ReflectionDomain;
import tech.intellispaces.core.ReflectionPoint;
import tech.intellispaces.core.Rid;
import tech.intellispaces.core.TraversableReflection;
import tech.intellispaces.core.TraversableReflectionPoint;
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
    public @Nullable TraversableReflection getReflection(String reflectionName) {
        return engine.getReflection(reflectionName);
    }

    @Override
    public @Nullable TraversableReflectionPoint getReflection(Rid pid, String domainName) {
        return engine.getReflection(pid, domainName);
    }

    @Override
    public <T> T castReflection(ReflectionPoint reflection, Class<T> reflectionClass) {
        return engine.castReflection(reflection, reflectionClass);
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
    public <S, T> T mapTo(S source, ReflectionDomain domain) {
        return engine.mapTo(source, domain);
    }

    @Override
    public TraversableReflectionPoint mapTo(Reflection source, ReflectionDomain targetDomain) {
        return engine.mapTo(source, targetDomain);
    }

    @Override
    public <R extends Reflection> R mapAndCastTo(
        Reflection source, ReflectionDomain targetDomain, Class<R> targetClass
    ) {
        return engine.mapAndCastTo(source, targetDomain, targetClass);
    }

    @Override
    public <Q, R extends Reflection> R mapAndCastTo(
        Reflection source, ReflectionDomain targetDomain, Q qualifier, Class<R> targetClass
    ) {
        return engine.mapAndCastTo(source, targetDomain, qualifier, targetClass);
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
    public <S, Q> void moveThruChannel1(S reflection, Rid channelId, Q qualifier) {
        engine.moveThruChannel1(reflection, channelId, qualifier);
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
