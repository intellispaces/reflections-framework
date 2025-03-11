package tech.intellispaces.jaquarius.engine.impl;

import tech.intellispaces.jaquarius.exception.ConfigurationExceptions;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceFunctions;
import tech.intellispaces.jaquarius.system.ProjectionInjection;
import tech.intellispaces.jaquarius.system.UnitProjectionDefinition;
import tech.intellispaces.jaquarius.system.injection.InjectionKinds;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The module validator.
 */
class ModuleValidator {

  static void validate(Module module) {
    checkThatOneMainUnit(module);
    checkThatThereAreNoProjectionsWithSameName(module);
    checkInjections(module);
  }

  static void checkThatOneMainUnit(Module module) {
    List<Unit> mainUnits = module.units().stream()
        .filter(tech.intellispaces.jaquarius.system.Unit::isMain)
        .toList();
    if (mainUnits.isEmpty()) {
      throw ConfigurationExceptions.withMessage("Main unit is not found");
    }
    if (mainUnits.size() > 1) {
      throw ConfigurationExceptions.withMessage("Multiple main units found: {0}",
          mainUnits.stream().map(tech.intellispaces.jaquarius.system.Unit::unitClass)
              .map(Class::getSimpleName)
              .collect(Collectors.joining(", ")));
    }
  }

  static void checkThatThereAreNoProjectionsWithSameName(Module module) {
    String message = module.units().stream()
        .map(tech.intellispaces.jaquarius.system.Unit::projectionDefinitions)
        .flatMap(List::stream)
        .collect(Collectors.groupingBy(UnitProjectionDefinition::name))
        .entrySet().stream()
        .filter(e -> e.getValue().size() > 1)
        .map(e -> makeSameProjectionsInfo(e.getKey(), e.getValue()))
        .collect(Collectors.joining("; "));
    if (!message.isEmpty()) {
      throw ConfigurationExceptions.withMessage("Found multiple projections with same name: {0}", message);
    }
  }

  static void checkInjections(Module module) {
    Map<String, UnitProjectionDefinition> projectionProviders = module.units().stream()
        .map(tech.intellispaces.jaquarius.system.Unit::projectionDefinitions)
        .flatMap(List::stream)
        .collect(Collectors.toMap(UnitProjectionDefinition::name, Function.identity()));

    checkUnitInjections(module, projectionProviders);
  }

  static void checkUnitInjections(
      Module module, Map<String, UnitProjectionDefinition> projectionProviders
  ) {
    List<ProjectionInjection> injections = module.units().stream()
        .map(Unit::injections)
        .flatMap(List::stream)
        .filter(injection -> InjectionKinds.Projection.is(injection.kind()))
        .map(injection -> (ProjectionInjection) injection)
        .toList();
    for (ProjectionInjection injection : injections) {
      UnitProjectionDefinition provider = projectionProviders.get(injection.name());
      if (provider == null) {
        throw ConfigurationExceptions.withMessage("Projection injection by name '{0}' declared in unit {1} " +
                "is not found", injection.name(), injection.unitClass().getCanonicalName());
      }
      if (!ObjectReferenceFunctions.isCompatibleObjectType(injection.targetClass(), provider.type())) {
        throw ConfigurationExceptions.withMessage("Projection injection '{0}' declared in unit {1} " +
                "has an incompatible target type. Expected type {2}, actual type {3}",
            injection.name(), injection.unitClass().getCanonicalName(),
            injection.targetClass().getCanonicalName(), provider.type().getCanonicalName());
      }
    }
  }

  static String makeSameProjectionsInfo(String projectionName, List<UnitProjectionDefinition> projectionProviders) {
    return "Projection name '" + projectionName +
        "', projection providers: " + projectionProviders.stream()
        .map(ModuleValidator::getProjectionProviderName)
        .collect(Collectors.joining(", "));
  }

  static String getProjectionProviderName(UnitProjectionDefinition projectionProvider) {
    return projectionProvider.unitClass().getCanonicalName() + "#" + projectionProvider.projectionMethod().getName();
  }
}
