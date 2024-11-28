package tech.intellispaces.jaquarius.validation;

import tech.intellispaces.jaquarius.annotation.Configuration;
import tech.intellispaces.jaquarius.annotation.Guide;
import tech.intellispaces.jaquarius.annotation.Module;
import tech.intellispaces.jaquarius.annotation.Projection;
import tech.intellispaces.jaquarius.annotation.ProjectionDefinition;
import tech.intellispaces.jaquarius.annotation.Shutdown;
import tech.intellispaces.jaquarius.annotation.Startup;
import tech.intellispaces.jaquarius.exception.JaquariusExceptions;
import tech.intellispaces.jaquarius.guide.GuideFunctions;
import tech.intellispaces.jaquarius.object.ObjectHandleFunctions;
import tech.intellispaces.jaquarius.system.ModuleFunctions;
import tech.intellispaces.java.annotation.validator.AnnotatedTypeValidator;
import tech.intellispaces.java.reflection.customtype.CustomType;
import tech.intellispaces.java.reflection.instance.AnnotationInstance;
import tech.intellispaces.java.reflection.method.MethodParam;
import tech.intellispaces.java.reflection.method.MethodStatement;
import tech.intellispaces.java.reflection.reference.TypeReference;

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
      if (!unitType.hasAnnotation(Module.class)) {
        throw JaquariusExceptions.withMessage("Class {0} is not marked with annotation {1}",
            unitType.canonicalName(), Module.class.getSimpleName());
      }
    } else {
      if (
          !unitType.hasAnnotation(Configuration.class) &&
              !unitType.hasAnnotation(Guide.class)
      ) {
        throw JaquariusExceptions.withMessage("Class {0} is not marked with annotation {1} or {2}",
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
      throw JaquariusExceptions.withMessage("Module unit {0} contains more that one startup methods",
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
      throw JaquariusExceptions.withMessage("Module unit {0} contains more that one shutdown methods",
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
      throw JaquariusExceptions.withMessage("Method of the projection '{0}' in unit {1} should " +
              "return value", method.name(), method.owner().canonicalName());
    }
    if (!ObjectHandleFunctions.isObjectHandleType(returnType.get())) {
      throw JaquariusExceptions.withMessage("Method of the projection '{0}' in unit {1} should " +
              "return object handle class", method.name(), method.owner().canonicalName());
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
      throw JaquariusExceptions.withMessage("Abstract method '{0}' in unit {1} should return value",
          method.name(), method.owner().canonicalName());
    }
    if (!method.params().isEmpty()) {
      throw JaquariusExceptions.withMessage("Abstract method '{0}' in unit {1} should have no parameters",
          method.name(), method.owner().canonicalName());
    }
    if (!ObjectHandleFunctions.isObjectHandleType(returnType.get()) && !GuideFunctions.isGuideType(returnType.get())) {
      throw JaquariusExceptions.withMessage("Injection '{0}' in unit {1} should return " +
              "object handle or guide class", method.name(), method.owner().canonicalName());
    }
  }

  private void checkAbstractProjectionProviderAnnotation(MethodStatement method) {
    List<AnnotationInstance> projectionDefinitionAnnotations = method.annotations().stream()
        .filter(a -> a.annotationStatement().hasAnnotation(ProjectionDefinition.class))
        .toList();
    if (projectionDefinitionAnnotations.isEmpty()) {
      throw JaquariusExceptions.withMessage("Abstract projection method '{0}' in unit {1} should " +
          "have a Projection Definition annotation", method.name(), method.owner().canonicalName());
    }
    if (projectionDefinitionAnnotations.size() > 1) {
      throw JaquariusExceptions.withMessage("Abstract projection method '{0}' in unit {1} should " +
          "have single Projection Definition annotation", method.name(), method.owner().canonicalName());
    }
  }

  private void checkMethodParams(MethodStatement method) {
    for (MethodParam param : method.params()) {
      TypeReference paramType = param.type();
      if (!ObjectHandleFunctions.isObjectHandleType(paramType)) {
        throw JaquariusExceptions.withMessage("Parameter '{0}' of method '{1}' in unit {2} should be " +
            "object handle class", param.name(), method.name(), method.owner().canonicalName());
      }
    }
  }

  private void checkThatIsNotUnitStartupMethod(MethodStatement method, CustomType unitType) {
    if (method.hasAnnotation(Startup.class)) {
      throw JaquariusExceptions.withMessage("Included unit should not have a starting method. " +
          "But method '{0}' in unit {1} is marked with annotation @{2}", method.name(), unitType.canonicalName(),
          Startup.class.getSimpleName());
    }
  }

  private void checkThatIsNotUnitShutdownMethod(MethodStatement method, CustomType unitType) {
    if (method.hasAnnotation(Shutdown.class)) {
      throw JaquariusExceptions.withMessage("Included unit should not have a shutdown method. " +
              "But method '{0}' in unit {1} is marked with annotation @{2}", method.name(), unitType.canonicalName(),
          Shutdown.class.getSimpleName());
    }
  }
}
