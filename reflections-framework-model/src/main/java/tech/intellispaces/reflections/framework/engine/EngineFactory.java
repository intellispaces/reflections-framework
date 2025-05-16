package tech.intellispaces.reflections.framework.engine;

/**
 * The engine factory.
 */
public interface EngineFactory {

    /**
     * Creates a new engine.
     *
     * @param args command line arguments.
     * @return created engine.
     */
    Engine create(String[] args);
}
