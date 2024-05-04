package tech.intellispacesframework.core.system;

import tech.intellispacesframework.core.exception.ConfigurationException;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Module validator.
 */
class ModuleValidator {

  public void validateModule(Module module) {
    checkThatOneMainUnit(module);
    checkThatOneStartupMethod(module);
    checkThatOneShutdownMethod(module);
    checkThatThereAreNoProjectionsWithSameName(module);
    checkInjections(module);
  }

  private void checkThatOneMainUnit(Module module) {
    List<Unit> mainUnits = module.units().stream()
        .filter(Unit::isMain)
        .toList();
    if (mainUnits.isEmpty()) {
      throw ConfigurationException.withMessage("Main unit was not found");
    }
    if (mainUnits.size() > 1) {
      throw ConfigurationException.withMessage("Multiple main units found: {}",
          mainUnits.stream().map(Unit::unitClass).map(Class::getSimpleName).collect(Collectors.joining(", ")));
    }
  }

  private void checkThatOneStartupMethod(Module module) {
    List<Method> methods = module.units().stream()
        .map(Unit::startupMethod)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();
    if (methods.size() > 1) {
      throw ConfigurationException.withMessage("Multiple startup methods found: {}",
          methods.stream().map(m -> m.getDeclaringClass().getSimpleName() + "#" + m.getName()).collect(Collectors.joining(", ")));
    }
  }

  private void checkThatOneShutdownMethod(Module module) {
    List<Method> methods = module.units().stream()
        .map(Unit::shutdownMethod)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();
    if (methods.size() > 1) {
      throw ConfigurationException.withMessage("Multiple shutdown methods found: {}",
          methods.stream().map(m -> m.getDeclaringClass().getSimpleName() + "#" + m.getName()).collect(Collectors.joining(", ")));
    }
  }

  private void checkThatThereAreNoProjectionsWithSameName(Module module) {
    String message = module.units().stream()
        .map(Unit::projectionProviders)
        .flatMap(List::stream)
        .collect(Collectors.groupingBy(UnitProjectionProvider::name))
        .entrySet().stream()
        .filter(e -> e.getValue().size() > 1)
        .map(e -> makeSameProjectionsInfo(e.getKey(), e.getValue()))
        .collect(Collectors.joining("; "));
    if (!message.isEmpty()) {
      throw ConfigurationException.withMessage("Found multiple projections with same name: {}", message);
    }
  }

  private void checkInjections(Module module) {




  }

  private String makeSameProjectionsInfo(String projectionName, List<UnitProjectionProvider> projectionProviders) {
    return "Projection name '" + projectionName +
        "', projection providers: " + projectionProviders.stream().map(this::getProjectionProviderName).collect(Collectors.joining(", "));
  }

  private String getProjectionProviderName(UnitProjectionProvider projectionProvider) {
    return projectionProvider.unit().unitClass().getCanonicalName() + "#" + projectionProvider.providerMethod().getName();
  }
}
