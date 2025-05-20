package tech.intellispaces.reflections.framework.node;

import tech.intellispaces.commons.data.Holder;
import tech.intellispaces.commons.data.SingleHolder;
import tech.intellispaces.reflections.framework.engine.Engine;
import tech.intellispaces.reflections.framework.engine.EngineRegistrar;
import tech.intellispaces.reflections.framework.engine.SingleEngineRegistrar;

/**
 * Functions related to the application node as a whole.
 */
public class NodeFunctions {
  private static final Holder<Engine> ENGINE_HOLDER = createEngineHolder();

  public static Holder<Engine> engineHolder() {
    return ENGINE_HOLDER;
  }

  public static EngineRegistrar createEngineRegistrar() {
    return new SingleEngineRegistrar();
  }

  static Holder<Engine> createEngineHolder() {
    return new SingleHolder<>();
  }

  private NodeFunctions() {}
}
