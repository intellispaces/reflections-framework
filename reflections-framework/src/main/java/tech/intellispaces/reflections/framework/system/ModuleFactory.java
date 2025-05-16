package tech.intellispaces.reflections.framework.system;

import java.util.List;

import tech.intellispaces.reflections.framework.engine.Engine;

public interface ModuleFactory {

    /**
     * Creates a new module.
     *
     * @param unitClasses module unit classes.
     * @param engine the engine.
     * @return created module.
     */
    static ModuleImpl createModule(List<Class<?>> unitClasses, Engine engine) {
        var module = new ModuleImpl();



        return module;
    }
}
