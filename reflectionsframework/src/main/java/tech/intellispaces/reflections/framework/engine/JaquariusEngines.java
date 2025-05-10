package tech.intellispaces.reflections.framework.engine;

import java.util.ServiceLoader;

import tech.intellispaces.commons.exception.UnexpectedExceptions;

public class JaquariusEngines {
  private static JaquariusEngine ENGINE = null;

  public static JaquariusEngine get() {
    if (ENGINE == null) {
      ENGINE = find();
    }
    return ENGINE;
  }

  private static JaquariusEngine find() {
    ServiceLoader<JaquariusEngine> serviceLoader = ServiceLoader.load(JaquariusEngine.class);
    return serviceLoader.findFirst().orElseThrow(() -> UnexpectedExceptions.withMessage(
        "The implementation of the Jaquarius Engine interface {0} is not found",
        JaquariusEngine.class.getCanonicalName()));
  }

  private JaquariusEngines() {}
}
