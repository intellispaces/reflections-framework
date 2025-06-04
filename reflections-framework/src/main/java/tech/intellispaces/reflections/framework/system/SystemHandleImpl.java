package tech.intellispaces.reflections.framework.system;

import tech.intellispaces.core.Domain;
import tech.intellispaces.core.Reflection;
import tech.intellispaces.core.ReflectionContract;
import tech.intellispaces.core.Rid;
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
    public Reflection create(ReflectionContract contract) {
        return engine.createReflection(contract);
    }

    @Override
    public <S, T> T mapSourceTo(S source, Domain domain) {
        return engine.mapSourceTo(source, domain);
    }

    @Override
    public Reflection mapSourceTo(Reflection source, Domain domain) {
        return engine.mapSourceTo(source, domain);
    }

    @Override
    public <R extends Reflection> R mapSourceTo(Reflection source, Domain domain, Class<R> targetClass) {
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
    public <S, T, Q, C extends Channel1 & MappingTraverse> T mapThruChannel1(S source, Class<C> channelClass, Q qualifier) {
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
    public <S, R, Q, C extends Channel1 & MappingOfMovingTraverse> R mapOfMovingThruChannel1(S source, Class<C> channelClass, Q qualifier) {
        return null;
    }

    @Override
    public <S, R, Q> R mapOfMovingThruChannel1(S source, Rid cid, Q qualifier) {
        return null;
    }
}
