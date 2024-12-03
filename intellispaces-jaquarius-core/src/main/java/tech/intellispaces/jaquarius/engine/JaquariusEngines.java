package tech.intellispaces.jaquarius.engine;

import tech.intellispaces.entity.exception.UnexpectedExceptions;

import java.util.ServiceLoader;

public class JaquariusEngines {
  private static JaquariusEngine ENGINE = null;

  public static JaquariusEngine get() {
    if (ENGINE == null) {
      ENGINE = findEngine();
    }
    return ENGINE;
  }

  private static JaquariusEngine findEngine() {
    ServiceLoader<JaquariusEngine> serviceLoader = ServiceLoader.load(JaquariusEngine.class);
    return serviceLoader.findFirst().orElseThrow(() -> UnexpectedExceptions.withMessage(
        "The implementation of the factory {0} is not provided", JaquariusEngine.class.getCanonicalName()));
  }

  private JaquariusEngines() {}
}
