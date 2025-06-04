package tech.intellispaces.reflections.framework.engine;

import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

import tech.intellispaces.reflections.framework.exception.ConfigurationExceptions;

public class Engines {

  /**
   * Creates new engine.
   *
   * @param args command line arguments.
   * @param engineAttributes engine attributes.
   * @return created engine.
   */
  public static Engine create(String[] args, Map<String, Object> engineAttributes) {
    return findFactory().create(args, engineAttributes);
  }

  static EngineFactory findFactory() {
    ServiceLoader<EngineFactory> serviceLoader = ServiceLoader.load(EngineFactory.class);
    List<ServiceLoader.Provider<EngineFactory>> providers = serviceLoader.stream().toList();
    if (providers.isEmpty()) {
      throw ConfigurationExceptions.withMessage("No engine factories are found");
    }
    if (providers.size() > 1) {
      throw ConfigurationExceptions.withMessage("Many engine factories have been found. " +
          "Only one engine factory should be defined." +
          providers.stream()
              .map(p -> "\n   - " + p.type().getCanonicalName())
              .collect(Collectors.joining(""))
          );
    }
    return providers.get(0).get();
  }

  private Engines() {}
}
