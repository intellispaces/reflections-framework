package tech.intellispaces.framework.core.validation;

import tech.intellispaces.framework.annotationprocessor.AnnotatedTypeValidator;
import tech.intellispaces.framework.core.annotation.Configuration;
import tech.intellispaces.framework.core.annotation.Guide;
import tech.intellispaces.framework.core.annotation.Module;
import tech.intellispaces.framework.core.annotation.Projection;
import tech.intellispaces.framework.core.annotation.ProjectionDefinition;
import tech.intellispaces.framework.core.annotation.Shutdown;
import tech.intellispaces.framework.core.annotation.Startup;
import tech.intellispaces.framework.core.exception.IntelliSpacesException;
import tech.intellispaces.framework.core.object.ObjectFunctions;
import tech.intellispaces.framework.core.system.ModuleFunctions;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;
import tech.intellispaces.framework.javastatements.statement.instance.AnnotationInstance;
import tech.intellispaces.framework.javastatements.statement.method.MethodParam;
import tech.intellispaces.framework.javastatements.statement.method.MethodStatement;
import tech.intellispaces.framework.javastatements.statement.reference.TypeReference;

import java.util.List;
import java.util.Optional;

/**
 * Module type validator.
 */
public class ModuleValidator implements AnnotatedTypeValidator {

  @Override
  public void validate(CustomType moduleType) {
    validateUnitTypeAnnotations(moduleType, true);
    validateStartupMethod(moduleType);
    validateShutdownMethod(moduleType);
    validateProjectionMethods(moduleType);
    validateAbstractMethods(moduleType);

    Iterable<CustomType> includedUnits = ModuleFunctions.getIncludedUnits(moduleType);
    validateIncludedUnitTypes(includedUnits);
  }

  private void validateUnitTypeAnnotations(CustomType unitType, boolean mainUnit) {
    if (mainUnit) {
      if (!unitType.hasAnnotation(tech.intellispaces.framework.core.annotation.Module.class)) {
        throw IntelliSpacesException.withMessage("Class {} is not marked with annotation {}",
            unitType.canonicalName(), Module.class.getSimpleName());
      }
    } else {
      if (
          !unitType.hasAnnotation(tech.intellispaces.framework.core.annotation.Configuration.class) &&
              !unitType.hasAnnotation(tech.intellispaces.framework.core.annotation.Guide.class)
      ) {
        throw IntelliSpacesException.withMessage("Class {} is not marked with annotation {} or {}",
            unitType.canonicalName(), Configuration.class.getSimpleName(), Guide.class.getSimpleName());
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
        .forEach(this::checkInjectedMethod);
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
          method.name(), method.owner().canonicalName());
    }
    if (!ObjectFunctions.isObjectHandleType(returnType.get())) {
      throw IntelliSpacesException.withMessage("Method of the projection '{}' in unit {} should return object handle class",
          method.name(), method.owner().canonicalName());
    }
    if (method.isAbstract()) {
      checkAbstractProjectionProviderAnnotation(method);
    } else {
      checkMethodParams(method);
    }
  }

  private void checkInjectedMethod(MethodStatement method) {
    Optional<TypeReference> returnType = method.returnType();
    if (returnType.isEmpty()) {
      throw IntelliSpacesException.withMessage("Abstract method '{}' in unit {} should return value",
          method.name(), method.owner().canonicalName());
    }
    if (!ObjectFunctions.isObjectHandleType(returnType.get())) {
      throw IntelliSpacesException.withMessage("Injection '{}' in unit {} should return object handle class",
          method.name(), method.owner().canonicalName());
    }
    if (!method.params().isEmpty()) {
      throw IntelliSpacesException.withMessage("Abstract method '{}' in unit {} should have no parameters",
          method.name(), method.owner().canonicalName());
    }
  }

  private void checkAbstractProjectionProviderAnnotation(MethodStatement method) {
    List<AnnotationInstance> projectionDefinitionAnnotations = method.annotations().stream()
        .filter(a -> a.annotationStatement().hasAnnotation(ProjectionDefinition.class))
        .toList();
    if (projectionDefinitionAnnotations.isEmpty()) {
      throw IntelliSpacesException.withMessage("Abstract projection method '{}' in unit {} should have a Projection " +
              "Definition annotation", method.name(), method.owner().canonicalName());
    }
    if (projectionDefinitionAnnotations.size() > 1) {
      throw IntelliSpacesException.withMessage("Abstract projection method '{}' in unit {} should have single Projection " +
          "Definition annotation", method.name(), method.owner().canonicalName());
    }
  }

  private void checkMethodParams(MethodStatement method) {
    for (MethodParam param : method.params()) {
      TypeReference paramType = param.type();
      if (!ObjectFunctions.isObjectHandleType(paramType)) {
        throw IntelliSpacesException.withMessage("Parameter '{}' of method '{}' in unit {} should be object handle " +
                "class", param.name(), method.name(), method.owner().canonicalName());
      }
    }
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
}
