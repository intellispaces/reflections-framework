package tech.intellispaces.reflections.framework.annotationprocessor.module;

import java.util.List;
import java.util.Optional;

import tech.intellispaces.annotationprocessor.ArtifactValidator;
import tech.intellispaces.core.System;
import tech.intellispaces.javareflection.customtype.CustomType;
import tech.intellispaces.javareflection.instance.AnnotationInstance;
import tech.intellispaces.javareflection.method.MethodParam;
import tech.intellispaces.javareflection.method.MethodStatement;
import tech.intellispaces.javareflection.reference.TypeReference;
import tech.intellispaces.reflections.framework.annotation.Configuration;
import tech.intellispaces.reflections.framework.annotation.Guide;
import tech.intellispaces.reflections.framework.annotation.Module;
import tech.intellispaces.reflections.framework.annotation.Projection;
import tech.intellispaces.reflections.framework.annotation.ProjectionSupplier;
import tech.intellispaces.reflections.framework.annotation.Shutdown;
import tech.intellispaces.reflections.framework.annotation.Startup;
import tech.intellispaces.reflections.framework.exception.ReflectionsExceptions;
import tech.intellispaces.reflections.framework.guide.GuideFunctions;
import tech.intellispaces.reflections.framework.reflection.ReflectionFunctions;
import tech.intellispaces.reflections.framework.system.ModuleFunctions;

/**
 * Module type validator.
 */
public class ModuleValidator implements ArtifactValidator {

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
        throw ReflectionsExceptions.withMessage("Class {0} is not marked with annotation {1}",
            unitType.canonicalName(), Module.class.getSimpleName());
      }
    } else {
      if (
          !unitType.hasAnnotation(Configuration.class) &&
              !unitType.hasAnnotation(Guide.class)
      ) {
        throw ReflectionsExceptions.withMessage("Class {0} is not marked with annotation {1} or {2}",
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
      throw ReflectionsExceptions.withMessage("Module unit {0} contains more that one startup methods",
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
      throw ReflectionsExceptions.withMessage("Module unit {0} contains more that one shutdown methods",
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
      throw ReflectionsExceptions.withMessage("Method of the projection '{0}' in unit {1} should " +
              "return value", method.name(), method.owner().canonicalName());
    }
    if (!ReflectionFunctions.isObjectFormType(returnType.get())) {
      throw ReflectionsExceptions.withMessage("Method of the projection '{0}' in unit {1} should " +
              "return reflection", method.name(), method.owner().canonicalName());
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
      throw ReflectionsExceptions.withMessage("Abstract method '{0}' in unit {1} should return value",
          method.name(), method.owner().canonicalName());
    }
    if (!method.params().isEmpty()) {
      throw ReflectionsExceptions.withMessage("Abstract method '{0}' in unit {1} should have no parameters",
          method.name(), method.owner().canonicalName());
    }
    if (
        !ReflectionFunctions.isObjectFormType(returnType.get())
          && !GuideFunctions.isGuideType(returnType.get())
          && ! isSystemTypeReference(returnType.get())
    ) {
      throw ReflectionsExceptions.withMessage("Injection '{0}' in unit {1} should return " +
              "reflection, guide or system class", method.name(), method.owner().canonicalName());
    }
  }

  private boolean isSystemTypeReference(TypeReference typeReference) {
    if (!typeReference.isCustomTypeReference()) {
      return false;
    }
    return System.class.getCanonicalName().equals(
        typeReference.asCustomTypeReference().orElseThrow().targetType().canonicalName()
    );
  }

  private void checkAbstractProjectionProviderAnnotation(MethodStatement method) {
    List<AnnotationInstance> projectionDefinitionAnnotations = method.annotations().stream()
        .filter(a -> a.annotationStatement().hasAnnotation(ProjectionSupplier.class))
        .toList();
    if (projectionDefinitionAnnotations.isEmpty()) {
      throw ReflectionsExceptions.withMessage("Abstract projection method '{0}' in unit {1} should " +
          "have a Projection Definition annotation", method.name(), method.owner().canonicalName());
    }
    if (projectionDefinitionAnnotations.size() > 1) {
      throw ReflectionsExceptions.withMessage("Abstract projection method '{0}' in unit {1} should " +
          "have single Projection Definition annotation", method.name(), method.owner().canonicalName());
    }
  }

  private void checkMethodParams(MethodStatement method) {
    for (MethodParam param : method.params()) {
      TypeReference paramType = param.type();
      if (!ReflectionFunctions.isObjectFormType(paramType)) {
        throw ReflectionsExceptions.withMessage("Parameter '{0}' of method '{1}' in unit {2} should be " +
            "reflection class", param.name(), method.name(), method.owner().canonicalName());
      }
    }
  }

  private void checkThatIsNotUnitStartupMethod(MethodStatement method, CustomType unitType) {
    if (method.hasAnnotation(Startup.class)) {
      throw ReflectionsExceptions.withMessage("Included unit should not have a starting method. " +
          "But method '{0}' in unit {1} is marked with annotation @{2}", method.name(), unitType.canonicalName(),
          Startup.class.getSimpleName());
    }
  }

  private void checkThatIsNotUnitShutdownMethod(MethodStatement method, CustomType unitType) {
    if (method.hasAnnotation(Shutdown.class)) {
      throw ReflectionsExceptions.withMessage("Included unit should not have a shutdown method. " +
              "But method '{0}' in unit {1} is marked with annotation @{2}", method.name(), unitType.canonicalName(),
          Shutdown.class.getSimpleName());
    }
  }
}
