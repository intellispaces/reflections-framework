package tech.intellispaces.core.annotation.processor;

import tech.intellispaces.annotations.generator.ArtifactGenerator;
import tech.intellispaces.commons.collection.ArraysFunctions;
import tech.intellispaces.commons.type.TypeFunctions;
import tech.intellispaces.core.annotation.Data;
import tech.intellispaces.core.annotation.Domain;
import tech.intellispaces.core.annotation.Module;
import tech.intellispaces.core.annotation.ObjectHandle;
import tech.intellispaces.core.annotation.Ontology;
import tech.intellispaces.core.annotation.Preprocessing;
import tech.intellispaces.core.annotation.Transition;
import tech.intellispaces.core.annotation.processor.data.UnmovableDataHandleGenerator;
import tech.intellispaces.core.annotation.processor.domain.CommonObjectHandleGenerator;
import tech.intellispaces.core.annotation.processor.domain.ObjectHandleBaselineGenerator;
import tech.intellispaces.core.annotation.processor.domain.DomainGuideGenerator;
import tech.intellispaces.core.annotation.processor.domain.DomainTransitionGenerator;
import tech.intellispaces.core.annotation.processor.domain.MovableObjectHandleGenerator;
import tech.intellispaces.core.annotation.processor.domain.UnmovableObjectHandleGenerator;
import tech.intellispaces.core.annotation.processor.domain.UnmovableUpwardObjectHandleGenerator;
import tech.intellispaces.core.annotation.processor.module.UnitWrapperGenerator;
import tech.intellispaces.core.annotation.processor.objecthandle.MovableObjectHandleImplImplGenerator;
import tech.intellispaces.core.annotation.processor.objecthandle.UnmovableObjectHandleImplGenerator;
import tech.intellispaces.core.annotation.processor.ontology.OntologyGuideGenerator;
import tech.intellispaces.core.annotation.processor.ontology.OntologyTransitionGenerator;
import tech.intellispaces.core.object.ObjectFunctions;
import tech.intellispaces.core.system.ModuleFunctions;
import tech.intellispaces.core.system.UnitFunctions;
import tech.intellispaces.core.traverse.TraverseTypes;
import tech.intellispaces.javastatements.AnnotatedStatement;
import tech.intellispaces.javastatements.JavaStatements;
import tech.intellispaces.javastatements.customtype.CustomType;
import tech.intellispaces.javastatements.instance.AnnotationInstance;
import tech.intellispaces.javastatements.method.MethodStatement;
import tech.intellispaces.javastatements.reference.CustomTypeReference;
import tech.intellispaces.javastatements.reference.NotPrimitiveReference;
import tech.intellispaces.javastatements.reference.ReferenceBound;
import tech.intellispaces.javastatements.reference.TypeReference;

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
        if (isAutoGenerationEnabled(domainType, ArtifactTypes.Transition, roundEnv)) {
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
    addObjectHandleBaselineGenerator(domainType, generators, roundEnv);
    addBasicObjectHandleGenerators(domainType, generators, roundEnv);
    addUpgradeObjectHandleGenerators(domainType, domainType, generators);
    return generators;
  }

  private static void addObjectHandleBaselineGenerator(
      CustomType domainType, List<ArtifactGenerator> generators, RoundEnvironment roundEnv
  ) {
    if (isAutoGenerationEnabled(domainType, ArtifactTypes.ObjectHandleBranch, roundEnv)) {
      generators.add(new ObjectHandleBaselineGenerator(domainType));
    }
  }

  private static void addBasicObjectHandleGenerators(
      CustomType domainType, List<ArtifactGenerator> generators, RoundEnvironment roundEnv
  ) {
    if (isAutoGenerationEnabled(domainType, ArtifactTypes.ObjectHandle, roundEnv)) {
      generators.add(new CommonObjectHandleGenerator(domainType));
    }
    if (isAutoGenerationEnabled(domainType, ArtifactTypes.MovableObjectHandle, roundEnv)) {
      generators.add(new MovableObjectHandleGenerator(domainType));
    }
    if (isAutoGenerationEnabled(domainType, ArtifactTypes.UnmovableObjectHandle, roundEnv)) {
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
    List<NotPrimitiveReference> baseTypeArguments = baseDomainType.typeArguments();
    if (baseTypeArguments.isEmpty()) {
      return;
    }
    boolean allTypeArgumentsAreCustomTypes = baseTypeArguments.stream()
        .allMatch(AnnotationProcessorFunctions::isCustomTypeRelated);
    if (!allTypeArgumentsAreCustomTypes) {
      return;
    }
    generators.add(new UnmovableUpwardObjectHandleGenerator(domainType, baseDomainType));

    addUpgradeObjectHandleGenerators(domainType, baseDomainType.targetType(), generators);
  }

  private static boolean isCustomTypeRelated(TypeReference type) {
    if (type.isCustomTypeReference()) {
      return true;
    }
    if (type.isNamedReference()) {
      List<ReferenceBound> extendedBounds = type.asNamedReferenceOrElseThrow().extendedBounds();
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
        if (isAutoGenerationEnabled(ontologyType, ArtifactTypes.Transition, roundEnv)) {
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
    List<CustomType> preprocessingClasses = PreprocessingAnnotationFunctions.getPreprocessingClasses(preprocessingAnnotation);
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

  static boolean isEnableMapperGuideGeneration(
      CustomType domainType, MethodStatement method, RoundEnvironment roundEnv
  ) {
    return isAutoGenerationEnabled(domainType, ArtifactTypes.Mapper, roundEnv) &&
        ArraysFunctions.contains(
            method.selectAnnotation(Transition.class).orElseThrow().allowedTraverseTypes(),
            TraverseTypes.Mapping);
  }

  static boolean isEnableMoverGuideGeneration(
      CustomType domainType, MethodStatement method, RoundEnvironment roundEnv
  ) {
    return isAutoGenerationEnabled(domainType, ArtifactTypes.Mover, roundEnv) &&
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
      CustomType annotatedType, ArtifactTypes artifact, RoundEnvironment roundEnv
  ) {
    List<AnnotationInstance> preprocessingAnnotations = roundEnv.getElementsAnnotatedWith(Preprocessing.class).stream()
        .map(JavaStatements::statement)
        .map(stm -> (AnnotatedStatement) stm)
        .map(stm -> stm.selectAnnotation(Preprocessing.class.getCanonicalName()))
        .map(Optional::orElseThrow)
        .filter(ann -> PreprocessingAnnotationFunctions.isPreprocessingAnnotationOf(ann, annotatedType.canonicalName()))
        .toList();
    if (preprocessingAnnotations.isEmpty()) {
      return true;
    }
    return preprocessingAnnotations.stream().allMatch(PreprocessingAnnotationFunctions::isPreprocessingEnabled);
  }

  static String getDomainClassLink(TypeReference type) {
    if (type.isPrimitiveReference()) {
      return "{@link " +
          TypeFunctions.getPrimitiveWrapperClass(type.asPrimitiveReferenceOrElseThrow().typename()).getSimpleName() +
          "}";
    } else if (type.isCustomTypeReference()) {
      return "{@link " + type.asCustomTypeReferenceOrElseThrow().targetType().simpleName() + "}";
    } else {
      return "Object";
    }
  }

  static boolean isVoidType(TypeReference type) {
    if (type.isCustomTypeReference()) {
      return Void.class.getCanonicalName().equals(type.asCustomTypeReferenceOrElseThrow().targetType().canonicalName());
    }
    return false;
  }
}
