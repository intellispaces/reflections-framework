package tech.intellispaces.framework.core.validate;

import tech.intellispaces.framework.core.annotation.Guide;
import tech.intellispaces.framework.core.annotation.Module;
import tech.intellispaces.framework.core.annotation.Projection;
import tech.intellispaces.framework.core.annotation.ProjectionDefinition;
import tech.intellispaces.framework.core.annotation.Shutdown;
import tech.intellispaces.framework.core.annotation.Startup;
import tech.intellispaces.framework.core.exception.ConfigurationException;
import tech.intellispaces.framework.core.exception.IntelliSpacesException;
import tech.intellispaces.framework.core.object.ObjectFunctions;
import tech.intellispaces.framework.core.space.domain.DomainFunctions;
import tech.intellispaces.framework.core.system.InjectionTypes;
import tech.intellispaces.framework.core.system.ModuleDefault;
import tech.intellispaces.framework.core.system.ModuleFunctions;
import tech.intellispaces.framework.core.system.ProjectionInjection;
import tech.intellispaces.framework.core.system.Unit;
import tech.intellispaces.framework.core.system.UnitProjectionDefinition;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;
import tech.intellispaces.framework.javastatements.statement.custom.MethodParam;
import tech.intellispaces.framework.javastatements.statement.custom.MethodStatement;
import tech.intellispaces.framework.javastatements.statement.instance.AnnotationInstance;
import tech.intellispaces.framework.javastatements.statement.reference.TypeReference;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ModuleValidator {

  public void validateModuleType(CustomType moduleType) {
    validateUnitTypeAnnotations(moduleType, true);
    validateStartupMethod(moduleType);
    validateShutdownMethod(moduleType);
    validateProjectionMethods(moduleType);
    validateAbstractMethods(moduleType);

    Iterable<CustomType> includedUnits = ModuleFunctions.getIncludedUnits(moduleType);
    validateIncludedUnitTypes(includedUnits);
  }

  public void validateModuleInstance(ModuleDefault module) {
    checkThatOneMainUnit(module);
    checkThatThereAreNoProjectionsWithSameName(module);
    checkInjections(module);
  }

  private void validateUnitTypeAnnotations(CustomType unitType, boolean mainUnit) {
    if (mainUnit) {
      if (!unitType.hasAnnotation(tech.intellispaces.framework.core.annotation.Module.class)) {
        throw IntelliSpacesException.withMessage("Class {} is not marked with annotation {}",
            unitType.canonicalName(), Module.class.getSimpleName());
      }
    } else {
      if (
          !unitType.hasAnnotation(tech.intellispaces.framework.core.annotation.Unit.class) &&
              !unitType.hasAnnotation(tech.intellispaces.framework.core.annotation.Guide.class)
      ) {
        throw IntelliSpacesException.withMessage("Class {} is not marked with annotation {} or {}",
            unitType.canonicalName(), Unit.class.getSimpleName(), Guide.class.getSimpleName());
      }
    }
  }

  private void validateIncludedUnitTypes(Iterable<CustomType> includedUnits) {
    includedUnits.forEach(this::validateIncludedUnitType);
  }

  private void validateIncludedUnitType(CustomType unitType) {
    validateUnitTypeAnnotations(unitType, false);
    validateProjectionMethods(unitType);
    validateAbstractMethods(unitType);
    for (MethodStatement method : unitType.declaredMethods()) {
      validateIncludedUnitTypeMethod(method, unitType);
    }
  }

  private void validateIncludedUnitTypeMethod(MethodStatement method, CustomType unitType) {
    checkThatIsNotUnitStartupMethod(method, unitType);
    checkThatIsNotUnitShutdownMethod(method, unitType);
  }

  private void validateProjectionMethods(CustomType unitType) {
    unitType.declaredMethods().stream()
        .filter(m -> m.hasAnnotation(Projection.class))
        .forEach(this::checkProjectionMethod);
  }

  private void validateAbstractMethods(CustomType unitType) {
    unitType.declaredMethods().stream()
        .filter(MethodStatement::isAbstract)
        .forEach(this::checkUnitAbstractMethod);
  }

  private void validateStartupMethod(CustomType moduleType) {
    List<MethodStatement> startupMethods = moduleType.declaredMethods().stream()
        .filter(m -> m.hasAnnotation(Startup.class))
        .toList();
    if (startupMethods.size() > 1) {
      throw IntelliSpacesException.withMessage("Module unit {} contains more that one startup methods",
          moduleType.canonicalName());
    }
    if (!startupMethods.isEmpty()) {
      checkStartupOrShutdownMethod(startupMethods.get(0));
    }
  }

  private void validateShutdownMethod(CustomType moduleType) {
    List<MethodStatement> shutdownMethods = moduleType.declaredMethods().stream()
        .filter(m -> m.hasAnnotation(Shutdown.class))
        .toList();
    if (shutdownMethods.size() > 1) {
      throw IntelliSpacesException.withMessage("Module unit {} contains more that one shutdown methods",
          moduleType.canonicalName());
    }
    if (!shutdownMethods.isEmpty()) {
      checkStartupOrShutdownMethod(shutdownMethods.get(0));
    }
  }

  private void checkStartupOrShutdownMethod(MethodStatement method) {
    checkMethodParams(method);
  }

  private void checkProjectionMethod(MethodStatement method) {
    Optional<TypeReference> returnType = method.returnType();
    if (returnType.isEmpty()) {
      throw IntelliSpacesException.withMessage("Method of the projection '{}' in unit {} should return value",
          method.name(), method.holder().canonicalName());
    }
    if (!isValidType(returnType.get())) {
      throw IntelliSpacesException.withMessage("Method of the projection '{}' in unit {} should return object handle or domain class",
          method.name(), method.holder().canonicalName());
    }
    if (method.isAbstract()) {
      checkAbstractProjectionProviderAnnotation(method);
    } else {
      checkMethodParams(method);
    }
  }

  private void checkUnitAbstractMethod(MethodStatement method) {
    Optional<TypeReference> returnType = method.returnType();
    if (returnType.isEmpty()) {
      throw IntelliSpacesException.withMessage("Abstract method '{}' in unit {} should return value",
          method.name(), method.holder().canonicalName());
    }
    if (!method.params().isEmpty()) {
      throw IntelliSpacesException.withMessage("Abstract method '{}' in unit {} should have no parameters",
          method.name(), method.holder().canonicalName());
    }
  }

  private void checkAbstractProjectionProviderAnnotation(MethodStatement method) {
    List<AnnotationInstance> projectionDefinitionAnnotations = method.annotations().stream()
        .filter(a -> a.annotationStatement().hasAnnotation(ProjectionDefinition.class))
        .toList();
    if (projectionDefinitionAnnotations.isEmpty()) {
      throw IntelliSpacesException.withMessage("Abstract projection method '{}' in unit {} should have a Projection " +
              "Definition annotation", method.name(), method.holder().canonicalName());
    }
    if (projectionDefinitionAnnotations.size() > 1) {
      throw IntelliSpacesException.withMessage("Abstract projection method '{}' in unit {} should have single Projection " +
          "Definition annotation", method.name(), method.holder().canonicalName());
    }
  }

  private void checkMethodParams(MethodStatement method) {
    for (MethodParam param : method.params()) {
      TypeReference paramType = param.type();
      if (!isValidType(paramType)) {
        throw IntelliSpacesException.withMessage("Parameter '{}' of method '{}' in unit {} should be object handle " +
                "or domain class", param.name(), method.name(), method.holder().canonicalName());
      }
    }
  }

  private boolean isValidType(TypeReference type) {
    return ObjectFunctions.isObjectHandleType(type) || DomainFunctions.isDomainType(type);
  }

  private void checkThatIsNotUnitStartupMethod(MethodStatement method, CustomType unitType) {
    if (method.hasAnnotation(Startup.class)) {
      throw IntelliSpacesException.withMessage("Included unit should not have a starting method. " +
          "But method '{}' in unit {} is marked with annotation @{}", method.name(), unitType.canonicalName(),
          Startup.class.getSimpleName());
    }
  }

  private void checkThatIsNotUnitShutdownMethod(MethodStatement method, CustomType unitType) {
    if (method.hasAnnotation(Shutdown.class)) {
      throw IntelliSpacesException.withMessage("Included unit should not have a shutdown method. " +
              "But method '{}' in unit {} is marked with annotation @{}", method.name(), unitType.canonicalName(),
          Shutdown.class.getSimpleName());
    }
  }

  private void checkThatOneMainUnit(ModuleDefault module) {
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
        throw ConfigurationException.withMessage("Projection injection by name '{}' declared in unit {} was not found",
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

  private void checkStartupMethodInjections(ModuleDefault module, Map<String, UnitProjectionDefinition> projectionProviders) {
    module.units().stream()
        .filter(Unit::isMain)
        .findFirst()
        .flatMap(Unit::startupMethod)
        .ifPresent(method -> checkMethodParamInjections(method, projectionProviders));
  }

  private void checkShutdownMethodInjections(ModuleDefault module, Map<String, UnitProjectionDefinition> projectionProviders) {
    module.units().stream()
        .filter(Unit::isMain)
        .findFirst()
        .flatMap(Unit::shutdownMethod)
        .ifPresent(method -> checkMethodParamInjections(method, projectionProviders));
  }

  private void checkMethodParamInjections(Method method, Map<String, UnitProjectionDefinition> projectionProviders) {
    for (Parameter param : method.getParameters()) {
      UnitProjectionDefinition provider = projectionProviders.get(param.getName());
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

  private String makeSameProjectionsInfo(String projectionName, List<UnitProjectionDefinition> projectionProviders) {
    return "Projection name '" + projectionName +
        "', projection providers: " + projectionProviders.stream().map(this::getProjectionProviderName).collect(Collectors.joining(", "));
  }

  private String getProjectionProviderName(UnitProjectionDefinition projectionProvider) {
    return projectionProvider.unit().unitClass().getCanonicalName() + "#" + projectionProvider.projectionMethod().getName();
  }
}
