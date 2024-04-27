package tech.intellispacesframework.core.system;

import tech.intellispacesframework.core.exception.ConfigurationException;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Module validator.
 */
class ModuleValidator {

  public void validateModule(SystemModule module) {
    checkThatOneMainUnit(module);
    checkThatOneStartupMethod(module);
    checkThatOneShutdownMethod(module);
    checkThatThereAreNoProjectionsWithSameName(module);
  }

  private void checkThatOneMainUnit(SystemModule module) {
    List<SystemUnit> mainUnits = module.units().stream()
        .filter(SystemUnit::isMain)
        .toList();
    if (mainUnits.isEmpty()) {
      throw ConfigurationException.withMessage("Main unit was not found");
    }
    if (mainUnits.size() > 1) {
      throw ConfigurationException.withMessage("Multiple main units found: {}",
          mainUnits.stream().map(SystemUnit::unitClass).map(Class::getSimpleName).collect(Collectors.joining(", ")));
    }
  }

  private void checkThatOneStartupMethod(SystemModule module) {
    List<Method> methods = module.units().stream()
        .map(SystemUnit::startupMethod)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();
    if (methods.size() > 1) {
      throw ConfigurationException.withMessage("Multiple startup methods found: {}",
          methods.stream().map(m -> m.getDeclaringClass().getSimpleName() + "#" + m.getName()).collect(Collectors.joining(", ")));
    }
  }

  private void checkThatOneShutdownMethod(SystemModule module) {
    List<Method> methods = module.units().stream()
        .map(SystemUnit::shutdownMethod)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();
    if (methods.size() > 1) {
      throw ConfigurationException.withMessage("Multiple shutdown methods found: {}",
          methods.stream().map(m -> m.getDeclaringClass().getSimpleName() + "#" + m.getName()).collect(Collectors.joining(", ")));
    }
  }

  private void checkThatThereAreNoProjectionsWithSameName(SystemModule module) {
    String message = module.units().stream()
        .map(SystemUnit::projectionProviders)
        .flatMap(List::stream)
        .collect(Collectors.groupingBy(UnitProjectionProvider::name))
        .entrySet().stream()
        .filter(e -> e.getValue().size() > 1)
        .map(e -> "Projection name '" + e.getKey() + "', projection providers: " + e.getValue().stream().map(this::getProjectionProviderName).collect(Collectors.joining(", ")))
        .collect(Collectors.joining("; "));
    if (!message.isEmpty()) {
      throw ConfigurationException.withMessage("Found multiple projections with same name: {}", message);
    }
  }

  private String getProjectionProviderName(UnitProjectionProvider pp) {
    return pp.unit().unitClass().getCanonicalName() + "#" + pp.providerMethod().getName();
  }
}
