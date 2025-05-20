package tech.intellispaces.reflections.framework.engine;

/**
 * The engine registrar.
 */
public interface EngineRegistrar {

  /**
   * Registers engine.
   *
   * @param engine the engine.
   * @return <code>true</code> if engine is registered or <code>false</code> if the value has already been registered.
   */
  boolean register(Engine engine);
}
