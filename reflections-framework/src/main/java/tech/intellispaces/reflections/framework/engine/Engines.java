package tech.intellispaces.reflections.framework.engine;

import java.util.ServiceLoader;

import tech.intellispaces.commons.exception.UnexpectedExceptions;

public class Engines {
  private static Engine ENGINE = null;

  public static Engine get() {
    if (ENGINE == null) {
      ENGINE = find();
    }
    return ENGINE;
  }

  private static Engine find() {
    ServiceLoader<Engine> serviceLoader = ServiceLoader.load(Engine.class);
    return serviceLoader.findFirst().orElseThrow(() -> UnexpectedExceptions.withMessage(
        "There are no engine implementations {0}", Engine.class.getCanonicalName()));
  }

  private Engines() {}
}
