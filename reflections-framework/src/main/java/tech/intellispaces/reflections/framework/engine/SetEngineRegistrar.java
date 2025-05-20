package tech.intellispaces.reflections.framework.engine;

import java.util.IdentityHashMap;
import java.util.Map;

public class SetEngineRegistrar implements EngineRegistrar {
  private static final Object VALUE = new Object();
  private final Map<Engine, Object> map = new IdentityHashMap<>();

  @Override
  public boolean register(Engine engine) {
    return (map.put(engine, VALUE) == null);
  }
}
