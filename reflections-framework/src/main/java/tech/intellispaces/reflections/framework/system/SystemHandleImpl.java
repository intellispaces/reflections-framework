package tech.intellispaces.reflections.framework.system;

import tech.intellispaces.core.Reflection;
import tech.intellispaces.core.ReflectionContract;
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
    public <S, T> T mapThruChannel0(S source, String cid) {
        return null;
    }

    @Override
    public <S, T, C extends Channel0 & MappingTraverse> T mapThruChannel0(S source, Class<C> channelClass) {
        return null;
    }

    @Override
    public <S, T, Q> T mapThruChannel1(S source, String cid, Q qualifier) {
        return null;
    }

    @Override
    public <S, T, Q, C extends Channel1 & MappingTraverse> T mapThruChannel1(S source, Class<C> channelClass, Q qualifier) {
        return null;
    }

    @Override
    public <S, R> R moveThruChannel0(S source, String cid) {
        return null;
    }

    @Override
    public <S, R, Q> R moveThruChannel1(S source, String cid, Q qualifier) {
        return null;
    }

    @Override
    public <S, R, Q, C extends Channel1 & MappingOfMovingTraverse> R mapOfMovingThruChannel1(S source, Class<C> channelClass, Q qualifier) {
        return null;
    }

    @Override
    public <S, R, Q> R mapOfMovingThruChannel1(S source, String cid, Q qualifier) {
        return null;
    }
}
