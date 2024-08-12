package tech.intellispaces.core.system;

import tech.intellispaces.core.exception.ConfigurationException;
import tech.intellispaces.core.object.ObjectFunctions;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Default module instance validator.
 */
public class ModuleValidator {

  public void validate(ModuleDefault module) {
    checkThatOneMainUnit(module);
    checkThatThereAreNoProjectionsWithSameName(module);
    checkInjections(module);
  }

  private void checkThatOneMainUnit(ModuleDefault module) {
    List<Unit> mainUnits = module.units().stream()
        .filter(Unit::isMain)
        .toList();
    if (mainUnits.isEmpty()) {
      throw ConfigurationException.withMessage("Main unit is not found");
    }
    if (mainUnits.size() > 1) {
      throw ConfigurationException.withMessage("Multiple main units found: {}",
          mainUnits.stream().map(Unit::unitClass).map(Class::getSimpleName).collect(Collectors.joining(", ")));
    }
  }

  private void checkThatThereAreNoProjectionsWithSameName(ModuleDefault module) {
    String message = module.units().stream()
        .map(Unit::projectionProviders)
        .flatMap(List::stream)
        .collect(Collectors.groupingBy(UnitProjectionDefinition::name))
        .entrySet().stream()
        .filter(e -> e.getValue().size() > 1)
        .map(e -> makeSameProjectionsInfo(e.getKey(), e.getValue()))
        .collect(Collectors.joining("; "));
    if (!message.isEmpty()) {
      throw ConfigurationException.withMessage("Found multiple projections with same name: {}", message);
    }
  }

  private void checkInjections(ModuleDefault module) {
    Map<String, UnitProjectionDefinition> projectionProviders = module.units().stream()
        .map(Unit::projectionProviders)
        .flatMap(List::stream)
        .collect(Collectors.toMap(UnitProjectionDefinition::name, Function.identity()));

    checkUnitInjections(module, projectionProviders);
    checkStartupMethodInjections(module, projectionProviders);
    checkShutdownMethodInjections(module, projectionProviders);
  }

  private void checkUnitInjections(ModuleDefault module, Map<String, UnitProjectionDefinition> projectionProviders) {
    List<ProjectionInjection> unitInjections = module.units().stream()
        .map(Unit::injections)
        .flatMap(List::stream)
        .filter(injection -> InjectionTypes.ProjectionInjection == injection.type())
        .map(inj -> (ProjectionInjection) inj)
        .toList();
    for (ProjectionInjection injection : unitInjections) {
      UnitProjectionDefinition provider = projectionProviders.get(injection.name());
      if (provider == null) {
        throw ConfigurationException.withMessage("Projection injection by name '{}' declared in unit {} is not found",
            injection.name(), injection.unitClass().getCanonicalName());
      }
      if (!ObjectFunctions.isCompatibleObjectType(injection.targetClass(), provider.type())) {
        throw ConfigurationException.withMessage("Projection injection '{}' declared in unit {} has an incompatible " +
                "target type. Expected type {}, actual type {}",
            injection.name(), injection.unitClass().getCanonicalName(),
            injection.targetClass().getCanonicalName(), provider.type().getCanonicalName());
      }
    }
  }

  private void checkStartupMethodInjections(
      ModuleDefault module, Map<String, UnitProjectionDefinition> projectionProviders
  ) {
    module.units().stream()
        .filter(Unit::isMain)
        .findFirst()
        .flatMap(Unit::startupAction)
        .ifPresent(action -> checkMethodParamInjections(action.method(), projectionProviders));
  }

  private void checkShutdownMethodInjections(
      ModuleDefault module, Map<String, UnitProjectionDefinition> projectionProviders
  ) {
    module.units().stream()
        .filter(Unit::isMain)
        .findFirst()
        .flatMap(Unit::shutdownAction)
        .ifPresent(action -> checkMethodParamInjections(action.method(), projectionProviders));
  }

  private void checkMethodParamInjections(Method method, Map<String, UnitProjectionDefinition> projectionProviders) {
    for (Parameter param : method.getParameters()) {
      UnitProjectionDefinition provider = projectionProviders.get(param.getName());
      if (provider == null) {
        throw ConfigurationException.withMessage("Method '{}' in unit {} requires injection '{}' that is not found",
            method.getName(), method.getDeclaringClass().getCanonicalName(), param.getName());
      }
      if (!ObjectFunctions.isCompatibleObjectType(param.getType(), provider.type())) {
        throw ConfigurationException.withMessage("Method '{}' in unit {} requires injection '{}' of type {}, " +
                "but actual injection has type {}",
            method.getName(), method.getDeclaringClass().getCanonicalName(), param.getName(),
            param.getType().getCanonicalName(), provider.type().getCanonicalName());
      }
    }
  }

  private String makeSameProjectionsInfo(String projectionName, List<UnitProjectionDefinition> projectionProviders) {
    return "Projection name '" + projectionName +
        "', projection providers: " + projectionProviders.stream().map(this::getProjectionProviderName).collect(Collectors.joining(", "));
  }

  private String getProjectionProviderName(UnitProjectionDefinition projectionProvider) {
    return projectionProvider.unit().unitClass().getCanonicalName() + "#" + projectionProvider.projectionMethod().getName();
  }
}
