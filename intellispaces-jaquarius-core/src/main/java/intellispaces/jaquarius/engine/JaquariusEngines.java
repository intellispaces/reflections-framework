package intellispaces.jaquarius.engine;

import intellispaces.common.base.exception.UnexpectedViolationException;

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
    return serviceLoader.findFirst().orElseThrow(() -> UnexpectedViolationException.withMessage(
        "The implementation of the factory {0} is not provided", JaquariusEngine.class.getCanonicalName()));
  }

  private JaquariusEngines() {}
}
