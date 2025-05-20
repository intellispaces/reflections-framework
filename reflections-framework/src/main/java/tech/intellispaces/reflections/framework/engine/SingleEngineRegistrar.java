package tech.intellispaces.reflections.framework.engine;

public class SingleEngineRegistrar implements EngineRegistrar {
  private Engine engine;

  @Override
  public boolean register(Engine engine) {
    boolean result = (this.engine == null || this.engine != engine);
    this.engine = engine;
    return result;
  }
}
