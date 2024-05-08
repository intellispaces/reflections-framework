package tech.intellispacesframework.core.system;

import tech.intellispacesframework.core.exception.ConfigurationException;
import tech.intellispacesframework.core.object.ObjectFunctions;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
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
    Map<String, UnitProjectionProvider> projectionProviders = module.units().stream()
        .map(Unit::projectionProviders)
        .flatMap(List::stream)
        .collect(Collectors.toMap(UnitProjectionProvider::name, Function.identity()));

    checkUnitInjections(module, projectionProviders);
    checkStartupMethodInjections(module, projectionProviders);
    checkShutdownMethodInjections(module, projectionProviders);
  }

  private void checkUnitInjections(Module module, Map<String, UnitProjectionProvider> projectionProviders) {
    List<ProjectionInjection> unitInjections = module.units().stream()
        .map(Unit::injections)
        .flatMap(List::stream)
        .filter(injection -> InjectionTypes.ProjectionInjection == injection.type())
        .map(inj -> (ProjectionInjection) inj)
        .toList();
    for (ProjectionInjection injection : unitInjections) {
      UnitProjectionProvider provider = projectionProviders.get(injection.name());
      if (provider == null) {
        throw ConfigurationException.withMessage("Projection injection by name '{}' declared in unit {} was not found",
            injection.name(), injection.unitClass().getCanonicalName());
      }
      if (!ObjectFunctions.isCompatibleObjectType(injection.targetClass(), provider.type())) {
        throw ConfigurationException.withMessage("Projection injection '{}' declared in unit {} has an incompatible target type. Expected type {}, actual type {}",
            injection.name(), injection.unitClass().getCanonicalName(),
            injection.targetClass().getCanonicalName(), provider.type().getCanonicalName());
      }
    }
  }

  private void checkStartupMethodInjections(Module module, Map<String, UnitProjectionProvider> projectionProviders) {
    module.units().stream()
        .filter(Unit::isMain)
        .findFirst()
        .flatMap(Unit::startupMethod)
        .ifPresent(method -> checkMethodParamInjections(method, projectionProviders));
  }

  private void checkShutdownMethodInjections(Module module, Map<String, UnitProjectionProvider> projectionProviders) {
    module.units().stream()
        .filter(Unit::isMain)
        .findFirst()
        .flatMap(Unit::shutdownMethod)
        .ifPresent(method -> checkMethodParamInjections(method, projectionProviders));
  }

  private void checkMethodParamInjections(Method method, Map<String, UnitProjectionProvider> projectionProviders) {
    for (Parameter param : method.getParameters()) {
      UnitProjectionProvider provider = projectionProviders.get(param.getName());
      if (provider == null) {
        throw ConfigurationException.withMessage("Injection '{}' required in method '{}' declared in unit {} was not found",
                param.getName(), method.getName(), method.getDeclaringClass().getCanonicalName());
      }
      if (!ObjectFunctions.isCompatibleObjectType(param.getType(), provider.type())) {
        throw ConfigurationException.withMessage("Injection '{}' required in method '{}' declared in unit {} has an incompatible target type. " +
                        "Expected type {}, actual type {}",
                param.getName(), method.getName(), method.getDeclaringClass().getCanonicalName(),
                param.getType().getCanonicalName(), provider.type().getCanonicalName());
      }
    }
  }

  private String makeSameProjectionsInfo(String projectionName, List<UnitProjectionProvider> projectionProviders) {
    return "Projection name '" + projectionName +
        "', projection providers: " + projectionProviders.stream().map(this::getProjectionProviderName).collect(Collectors.joining(", "));
  }

  private String getProjectionProviderName(UnitProjectionProvider projectionProvider) {
    return projectionProvider.unit().unitClass().getCanonicalName() + "#" + projectionProvider.providerMethod().getName();
  }
}
