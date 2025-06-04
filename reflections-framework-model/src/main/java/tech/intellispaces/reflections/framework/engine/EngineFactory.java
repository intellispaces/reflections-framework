package tech.intellispaces.reflections.framework.engine;

import java.util.Map;

/**
 * The engine factory.
 */
public interface EngineFactory {

    /**
     * Creates a new engine.
     *
     * @param args command line arguments.
     * @param engineAttributes additional attributes for creating an engine.
     * @return created engine.
     */
    Engine create(String[] args, Map<String, Object> engineAttributes);
}
