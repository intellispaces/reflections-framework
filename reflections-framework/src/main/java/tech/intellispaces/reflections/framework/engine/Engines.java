package tech.intellispaces.reflections.framework.engine;

import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

import tech.intellispaces.commons.exception.UnexpectedExceptions;

public class Engines {
  private static Engine ENGINE = null;

  public static Engine create(String[] args) {
    return findFactory().create( args);
  }

  private static EngineFactory findFactory() {
    ServiceLoader<EngineFactory> serviceLoader = ServiceLoader.load(EngineFactory.class);
    List<ServiceLoader.Provider<EngineFactory>> providers = serviceLoader.stream().toList();
    if (providers.isEmpty()) {
      throw UnexpectedExceptions.withMessage("No engine factories are found");
    }
    if (providers.size() > 1) {
      throw UnexpectedExceptions.withMessage("Many engine factories have been found. " +
          "Only one engine factory should be defined." +
          providers.stream()
              .map(p -> "\n   - " + p.type().getCanonicalName())
              .collect(Collectors.joining(""))
          );
    }
    return providers.get(0).get();
  }



  public static Engine get() {
    if (ENGINE == null) {
      ENGINE = find();
    }
    return ENGINE;
  }

  private static Engine find() {
    ServiceLoader<Engine> serviceLoader = ServiceLoader.load(Engine.class);
    List<ServiceLoader.Provider<Engine>> providers = serviceLoader.stream().toList();
    if (providers.isEmpty()) {
      throw UnexpectedExceptions.withMessage("No engines are found");
    }
    if (providers.size() > 1) {
      throw UnexpectedExceptions.withMessage("Many engines have been found. " +
              "Only one engine should be defined." +
              providers.stream()
                      .map(p -> "\n   - " + p.type().getCanonicalName())
                      .collect(Collectors.joining(""))
      );
    }
    return providers.get(0).get();
  }

  private Engines() {}
}
