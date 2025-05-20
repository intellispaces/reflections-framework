package tech.intellispaces.reflections.framework.system;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import tech.intellispaces.commons.type.ClassFunctions;
import tech.intellispaces.core.Unit;
import tech.intellispaces.reflections.framework.exception.ConfigurationExceptions;
import tech.intellispaces.reflections.framework.system.injection.InjectionKinds;

/**
 * The module validator.
 */
public class ModuleValidator {

  public static void validateModule(ModuleHandleImpl module) {
    checkThatOneMainUnit(module);
    checkThatThereAreNoProjectionsWithSameName(module);
    checkInjections(module);
  }

  static void checkThatOneMainUnit(ModuleHandleImpl module) {
    List<UnitHandle> mainUnits = module.unitHandles().stream()
        .filter(Unit::isMain)
        .toList();
    if (mainUnits.isEmpty()) {
      throw ConfigurationExceptions.withMessage("Main unit is not found");
    }
    if (mainUnits.size() > 1) {
      throw ConfigurationExceptions.withMessage("Multiple main units found: {0}",
          mainUnits.stream().map(UnitHandle::unitClass)
              .map(Class::getSimpleName)
              .collect(Collectors.joining(", ")));
    }
  }

  static void checkThatThereAreNoProjectionsWithSameName(ModuleHandleImpl module) {
    String message = module.unitHandles().stream()
        .map(UnitHandle::projectionDefinitions)
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

  static void checkInjections(ModuleHandleImpl module) {
    Map<String, UnitProjectionDefinition> projectionDefinitions = module.unitHandles().stream()
        .map(UnitHandle::projectionDefinitions)
        .flatMap(List::stream)
        .collect(Collectors.toMap(UnitProjectionDefinition::name, Function.identity()));

    checkUnitInjections(module, projectionDefinitions);
  }

  static void checkUnitInjections(
      ModuleHandleImpl module, Map<String, UnitProjectionDefinition> projectionDefinitions
  ) {
    List<ProjectionInjection> injections = module.unitHandles().stream()
        .map(UnitHandle::injections)
        .flatMap(List::stream)
        .filter(injection -> InjectionKinds.Projection.is(injection.kind()))
        .map(injection -> (ProjectionInjection) injection)
        .toList();
    for (ProjectionInjection injection : injections) {
      UnitProjectionDefinition pd = projectionDefinitions.get(injection.name());
      if (pd == null) {
        throw ConfigurationExceptions.withMessage("Projection injection by name '{0}' declared in unit {1} " +
                "is not found", injection.name(), injection.unitClass().getCanonicalName());
      }
      if (!ClassFunctions.isCompatibleClasses(injection.targetClass(), pd.type())) {
        throw ConfigurationExceptions.withMessage("Projection injection '{0}' declared in unit {1} " +
                "has an incompatible target type. Expected type {2}, actual type {3}",
            injection.name(), injection.unitClass().getCanonicalName(),
            injection.targetClass().getCanonicalName(), pd.type().getCanonicalName());
      }
    }
  }

  static String makeSameProjectionsInfo(String projectionName, List<UnitProjectionDefinition> projectionProviders) {
    return "Projection name '" + projectionName +
        "', projection definitions: " + projectionProviders.stream()
        .map(ModuleValidator::getProjectionDefinitionName)
        .collect(Collectors.joining(", "));
  }

  static String getProjectionDefinitionName(UnitProjectionDefinition projectionDefinition) {
    return projectionDefinition.unitClass().getCanonicalName() + "#" + projectionDefinition.projectionMethod().getName();
  }
}
