package tech.intellispaces.framework.core.annotation.processor;

import tech.intellispaces.framework.annotationprocessor.generator.ArtifactGenerator;
import tech.intellispaces.framework.commons.collection.ArraysFunctions;
import tech.intellispaces.framework.commons.type.TypeFunctions;
import tech.intellispaces.framework.core.annotation.Data;
import tech.intellispaces.framework.core.annotation.Domain;
import tech.intellispaces.framework.core.annotation.Module;
import tech.intellispaces.framework.core.annotation.ObjectHandle;
import tech.intellispaces.framework.core.annotation.Ontology;
import tech.intellispaces.framework.core.annotation.Preprocessing;
import tech.intellispaces.framework.core.annotation.Transition;
import tech.intellispaces.framework.core.annotation.processor.data.UnmovableDataHandleGenerator;
import tech.intellispaces.framework.core.annotation.processor.domain.CommonObjectHandleGenerator;
import tech.intellispaces.framework.core.annotation.processor.domain.DomainGuideGenerator;
import tech.intellispaces.framework.core.annotation.processor.domain.DomainTransitionGenerator;
import tech.intellispaces.framework.core.annotation.processor.domain.MovableDowngradeObjectHandleGenerator;
import tech.intellispaces.framework.core.annotation.processor.domain.MovableObjectHandleGenerator;
import tech.intellispaces.framework.core.annotation.processor.domain.UnmovableObjectHandleGenerator;
import tech.intellispaces.framework.core.annotation.processor.domain.UnmovableUpgradeObjectHandleGenerator;
import tech.intellispaces.framework.core.annotation.processor.module.UnitWrapperGenerator;
import tech.intellispaces.framework.core.annotation.processor.objecthandle.MovableObjectHandleImplImplGenerator;
import tech.intellispaces.framework.core.annotation.processor.objecthandle.UnmovableObjectHandleImplGenerator;
import tech.intellispaces.framework.core.annotation.processor.ontology.OntologyGuideGenerator;
import tech.intellispaces.framework.core.annotation.processor.ontology.OntologyTransitionGenerator;
import tech.intellispaces.framework.core.object.ObjectFunctions;
import tech.intellispaces.framework.core.system.ModuleFunctions;
import tech.intellispaces.framework.core.system.UnitFunctions;
import tech.intellispaces.framework.core.traverse.TraverseTypes;
import tech.intellispaces.framework.javastatements.JavaStatements;
import tech.intellispaces.framework.javastatements.statement.AnnotatedStatement;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;
import tech.intellispaces.framework.javastatements.statement.instance.AnnotationInstance;
import tech.intellispaces.framework.javastatements.statement.instance.ClassInstance;
import tech.intellispaces.framework.javastatements.statement.instance.Instance;
import tech.intellispaces.framework.javastatements.statement.method.MethodStatement;
import tech.intellispaces.framework.javastatements.statement.reference.CustomTypeReference;
import tech.intellispaces.framework.javastatements.statement.reference.NonPrimitiveTypeReference;
import tech.intellispaces.framework.javastatements.statement.reference.TypeBoundReference;
import tech.intellispaces.framework.javastatements.statement.reference.TypeReference;

import javax.annotation.processing.RoundEnvironment;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface AnnotationProcessorFunctions {

  static List<ArtifactGenerator> makeDataArtifactGenerators(CustomType dataType) {
    List<ArtifactGenerator> generators = new ArrayList<>();
    generators.add(new UnmovableDataHandleGenerator(dataType));
    return generators;
  }

  static List<ArtifactGenerator> makeDomainArtifactGenerators(
      CustomType domainType, RoundEnvironment roundEnv
  ) {
    List<ArtifactGenerator> generators = new ArrayList<>();
    for (MethodStatement method : domainType.declaredMethods()) {
      if (method.hasAnnotation(Transition.class)) {
        if (isAutoGenerationEnabled(domainType, "Transition", roundEnv)) {
          generators.add(new DomainTransitionGenerator(domainType, method));
        }
        if (isEnableMapperGuideGeneration(domainType, method, roundEnv)) {
          generators.add(new DomainGuideGenerator(TraverseTypes.Mapping, domainType, method));
        }
        if (isEnableMoverGuideGeneration(domainType, method, roundEnv)) {
          generators.add(new DomainGuideGenerator(TraverseTypes.Moving, domainType, method));
        }
      }
    }
    addBasicObjectHandleGenerators(domainType, generators, roundEnv);
    addUpgradeObjectHandleGenerators(domainType, domainType, generators);
    addDowngradeObjectHandleGenerators(domainType, generators);
    return generators;
  }

  private static void addBasicObjectHandleGenerators(
      CustomType domainType, List<ArtifactGenerator> generators, RoundEnvironment roundEnv
  ) {
    if (isAutoGenerationEnabled(domainType, "ObjectHandle", roundEnv)) {
      generators.add(new CommonObjectHandleGenerator(domainType));
    }
    if (isAutoGenerationEnabled(domainType, "MovableObjectHandle", roundEnv)) {
      generators.add(new MovableObjectHandleGenerator(domainType));
    }
    if (isAutoGenerationEnabled(domainType, "UnmovableObjectHandle", roundEnv)) {
      generators.add(new UnmovableObjectHandleGenerator(domainType));
    }
  }

  private static void addUpgradeObjectHandleGenerators(
      CustomType domainType, CustomType curDomainType, List<ArtifactGenerator> generators
  ) {
    List<CustomTypeReference> parents = curDomainType.parentTypes();
    if (parents.size() != 1) {
      return;
    }
    CustomTypeReference baseDomainType = parents.get(0);
    List<NonPrimitiveTypeReference> baseTypeArguments = baseDomainType.typeArguments();
    if (baseTypeArguments.isEmpty()) {
      return;
    }
    boolean allTypeArgumentsAreCustomTypes = baseTypeArguments.stream()
        .allMatch(AnnotationProcessorFunctions::isCustomTypeRelated);
    if (!allTypeArgumentsAreCustomTypes) {
      return;
    }
    generators.add(new UnmovableUpgradeObjectHandleGenerator(domainType, baseDomainType));

    addUpgradeObjectHandleGenerators(domainType, baseDomainType.targetType(), generators);
  }

  private static void addDowngradeObjectHandleGenerators(
      CustomType domainType, List<ArtifactGenerator> generators
  ) {
    List<CustomTypeReference> parents = domainType.parentTypes();
    if (parents.size() != 1) {
      return;
    }
    CustomTypeReference baseDomainType = parents.get(0);

    generators.add(new MovableDowngradeObjectHandleGenerator(domainType, baseDomainType));
  }

  private static boolean isCustomTypeRelated(TypeReference type) {
    if (type.isCustomTypeReference()) {
      return true;
    }
    if (type.isNamedTypeReference()) {
      List<TypeBoundReference> extendedBounds = type.asNamedTypeReferenceSurely().extendedBounds();
      if (extendedBounds.size() == 1 && extendedBounds.get(0).isCustomTypeReference()) {
        return true;
      }
    }
    return false;
  }

  static List<ArtifactGenerator> makeOntologyArtifactGenerators(
      CustomType ontologyType, RoundEnvironment roundEnv
  ) {
    List<ArtifactGenerator> generators = new ArrayList<>();
    for (MethodStatement method : ontologyType.declaredMethods()) {
      if (method.hasAnnotation(Transition.class)) {
        if (isAutoGenerationEnabled(ontologyType, "Transition", roundEnv)) {
          generators.add(new OntologyTransitionGenerator(ontologyType, method));
        }
        if (isEnableMapperGuideGeneration(ontologyType, method, roundEnv)) {
          generators.add(new OntologyGuideGenerator(TraverseTypes.Mapping, ontologyType, method));
        }
        if (isEnableMoverGuideGeneration(ontologyType, method, roundEnv)) {
          generators.add(new OntologyGuideGenerator(TraverseTypes.Moving, ontologyType, method));
        }
      }
    }
    return generators;
  }

  static List<ArtifactGenerator> makeObjectHandleArtifactGenerators(CustomType objectHandleType) {
    if (ObjectFunctions.isMovableObjectHandle(objectHandleType)) {
      return List.of(new MovableObjectHandleImplImplGenerator(objectHandleType));
    } else {
      return List.of(new UnmovableObjectHandleImplGenerator(objectHandleType));
    }
  }

  static List<ArtifactGenerator> makeModuleArtifactGenerators(CustomType moduleType) {
    List<ArtifactGenerator> generators = new ArrayList<>();
    generators.add(new UnitWrapperGenerator(moduleType));
    Iterable<CustomType> includedUnits = ModuleFunctions.getIncludedUnits(moduleType);
    includedUnits.forEach(u -> generators.add(new UnitWrapperGenerator(u)));
    return generators;
  }

  static List<ArtifactGenerator> makePreprocessingArtifactGenerators(
      CustomType customType, RoundEnvironment roundEnv
  ) {
    AnnotationInstance preprocessingAnnotation = customType.selectAnnotation(
        Preprocessing.class.getCanonicalName()).orElseThrow();
    List<CustomType> preprocessingClasses = getPreprocessingClasses(preprocessingAnnotation);
    if (preprocessingClasses.isEmpty()) {
      return List.of();
    }

    List<ArtifactGenerator> generators = new ArrayList<>();
    for (CustomType preprocessingClass : preprocessingClasses) {
      if (preprocessingClass.hasAnnotation(Data.class)) {
        generators.addAll(makeDataArtifactGenerators(preprocessingClass));
      } else if (preprocessingClass.hasAnnotation(Domain.class)) {
        generators.addAll(makeDomainArtifactGenerators(preprocessingClass, roundEnv));
      } else  if (preprocessingClass.hasAnnotation(Module.class)) {
        generators.addAll(makeModuleArtifactGenerators(preprocessingClass));
      } else if (UnitFunctions.isUnitType(preprocessingClass)) {
        generators.add(new UnitWrapperGenerator(preprocessingClass));
      } else if (preprocessingClass.hasAnnotation(ObjectHandle.class)) {
        generators.addAll(makeObjectHandleArtifactGenerators(preprocessingClass));
      } else if (preprocessingClass.hasAnnotation(Ontology.class)) {
        generators.addAll(makeOntologyArtifactGenerators(preprocessingClass, roundEnv));
      }
    }
    return generators;
  }

  static boolean isPreprocessingEnabled(
      AnnotationInstance preprocessingAnnotation, String canonicalClassName
  ) {
    List<CustomType> preprocessingClasses = getPreprocessingClasses(preprocessingAnnotation);
    for (CustomType aClass : preprocessingClasses) {
      if (canonicalClassName.equals(aClass.canonicalName())) {
        return true;
      }
    }
    return false;
  }

  static boolean isPreprocessingEnabled(AnnotationInstance preprocessingAnnotation) {
    Object enabled = preprocessingAnnotation.elementValue("enable").orElseThrow()
        .asPrimitive().orElseThrow()
        .value();
    return Boolean.TRUE == enabled;
  }

  static List<CustomType> getPreprocessingClasses(AnnotationInstance preprocessingAnnotation) {
    return preprocessingAnnotation.elementValue("value").orElseThrow()
        .asArray().orElseThrow()
        .elements().stream()
        .map(Instance::asClass)
        .map(Optional::orElseThrow)
        .map(ClassInstance::type)
        .toList();
  }

  static boolean isEnableMapperGuideGeneration(
      CustomType domainType, MethodStatement method, RoundEnvironment roundEnv
  ) {
    return isAutoGenerationEnabled(domainType, "Mapper", roundEnv) &&
        ArraysFunctions.contains(
            method.selectAnnotation(Transition.class).orElseThrow().allowedTraverseTypes(),
            TraverseTypes.Mapping);
  }

  static boolean isEnableMoverGuideGeneration(
      CustomType domainType, MethodStatement method, RoundEnvironment roundEnv
  ) {
    return isAutoGenerationEnabled(domainType, "Mover", roundEnv) &&
        ArraysFunctions.contains(
            method.selectAnnotation(Transition.class).orElseThrow().allowedTraverseTypes(),
            TraverseTypes.Moving);
  }

  static boolean isAutoGenerationEnabled(CustomType annotatedType) {
    return annotatedType.selectAnnotation(Preprocessing.class)
        .map(Preprocessing::enable).
        orElse(true);
  }

  static boolean isAutoGenerationEnabled(
      CustomType annotatedType, String artifact, RoundEnvironment roundEnv
  ) {
    List<AnnotationInstance> preprocessingAnnotations = roundEnv.getElementsAnnotatedWith(Preprocessing.class).stream()
        .map(JavaStatements::statement)
        .map(stm -> (AnnotatedStatement) stm)
        .map(stm -> stm.selectAnnotation(Preprocessing.class.getCanonicalName()))
        .map(Optional::orElseThrow)
        .filter(ann -> isPreprocessingEnabled(ann, annotatedType.canonicalName()))
        .toList();
    if (preprocessingAnnotations.isEmpty()) {
      return true;
    }
    return preprocessingAnnotations.stream().allMatch(AnnotationProcessorFunctions::isPreprocessingEnabled);
  }

  static String getDomainClassLink(TypeReference type) {
    if (type.isPrimitive()) {
      return "{@link " +
          TypeFunctions.getPrimitiveWrapperClass(type.asPrimitiveTypeReferenceSurely().typename()).getSimpleName() +
          "}";
    } else if (type.isCustomTypeReference()) {
      return "{@link " + type.asCustomTypeReferenceSurely().targetType().simpleName() + "}";
    } else {
      return "Object";
    }
  }

  static boolean isVoidType(TypeReference type) {
    if (type.isCustomTypeReference()) {
      return Void.class.getCanonicalName().equals(type.asCustomTypeReferenceSurely().targetType().canonicalName());
    }
    return false;
  }
}
