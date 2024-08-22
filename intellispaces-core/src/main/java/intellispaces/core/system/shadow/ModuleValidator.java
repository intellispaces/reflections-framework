package intellispaces.core.system.shadow;

import intellispaces.core.exception.ConfigurationException;
import intellispaces.core.object.ObjectFunctions;
import intellispaces.core.system.Unit;
import intellispaces.core.system.UnitProjectionDefinition;
import intellispaces.core.system.UnitProjectionInjection;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Default module instance validator.
 */
public class ModuleValidator {

  public void validate(ShadowModule module) {
    checkThatOneMainUnit(module);
    checkThatThereAreNoProjectionsWithSameName(module);
    checkInjections(module);
  }

  private void checkThatOneMainUnit(ShadowModule module) {
    List<ShadowUnit> mainUnits = module.units().stream()
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

  private void checkThatThereAreNoProjectionsWithSameName(ShadowModule module) {
    String message = module.units().stream()
        .map(Unit::projectionDefinitions)
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

  private void checkInjections(ShadowModule module) {
    Map<String, UnitProjectionDefinition> projectionProviders = module.units().stream()
        .map(Unit::projectionDefinitions)
        .flatMap(List::stream)
        .collect(Collectors.toMap(UnitProjectionDefinition::name, Function.identity()));

    checkUnitInjections(module, projectionProviders);
  }

  private void checkUnitInjections(
      ShadowModule module, Map<String, UnitProjectionDefinition> projectionProviders
  ) {
    List<UnitProjectionInjection> unitInjections = module.units().stream()
        .map(ShadowUnit::projectionInjections)
        .flatMap(List::stream)
        .toList();
    for (UnitProjectionInjection injection : unitInjections) {
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

  private String makeSameProjectionsInfo(String projectionName, List<UnitProjectionDefinition> projectionProviders) {
    return "Projection name '" + projectionName +
        "', projection providers: " + projectionProviders.stream().map(this::getProjectionProviderName).collect(Collectors.joining(", "));
  }

  private String getProjectionProviderName(UnitProjectionDefinition projectionProvider) {
    return projectionProvider.unitClass().getCanonicalName() + "#" + projectionProvider.projectionMethod().getName();
  }
}
