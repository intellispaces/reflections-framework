package intellispaces.core.annotation.processor;

import intellispaces.annotations.AnnotatedTypeProcessor;
import intellispaces.annotations.generator.ArtifactGenerator;
import intellispaces.commons.collection.ArraysFunctions;
import intellispaces.commons.type.TypeFunctions;
import intellispaces.core.annotation.AnnotationProcessor;
import intellispaces.core.annotation.Data;
import intellispaces.core.annotation.Domain;
import intellispaces.core.annotation.Module;
import intellispaces.core.annotation.MovableObjectHandle;
import intellispaces.core.annotation.Ontology;
import intellispaces.core.annotation.Preprocessing;
import intellispaces.core.annotation.Transition;
import intellispaces.core.annotation.UnmovableObjectHandle;
import intellispaces.core.annotation.processor.data.UnmovableDataHandleGenerator;
import intellispaces.core.annotation.processor.domain.CommonObjectHandleGenerator;
import intellispaces.core.annotation.processor.domain.DomainGuideGenerator;
import intellispaces.core.annotation.processor.domain.DomainTransitionGenerator;
import intellispaces.core.annotation.processor.domain.MovableDownwardObjectHandleGenerator;
import intellispaces.core.annotation.processor.domain.MovableObjectHandleGenerator;
import intellispaces.core.annotation.processor.domain.ObjectHandleBunchGenerator;
import intellispaces.core.annotation.processor.domain.UnmovableObjectHandleGenerator;
import intellispaces.core.annotation.processor.domain.UnmovableUpwardObjectHandleGenerator;
import intellispaces.core.annotation.processor.objecthandle.MovableObjectHandleImplGenerator;
import intellispaces.core.annotation.processor.objecthandle.UnmovableObjectHandleImplGenerator;
import intellispaces.core.annotation.processor.ontology.OntologyGuideGenerator;
import intellispaces.core.annotation.processor.ontology.OntologyTransitionGenerator;
import intellispaces.core.annotation.processor.unit.UnitWrapperGenerator;
import intellispaces.core.space.domain.DomainFunctions;
import intellispaces.core.system.ModuleFunctions;
import intellispaces.core.system.UnitFunctions;
import intellispaces.core.traverse.TraverseTypes;
import intellispaces.javastatements.AnnotatedStatement;
import intellispaces.javastatements.JavaStatements;
import intellispaces.javastatements.customtype.AnnotationFunctions;
import intellispaces.javastatements.customtype.CustomType;
import intellispaces.javastatements.instance.AnnotationInstance;
import intellispaces.javastatements.instance.ClassInstance;
import intellispaces.javastatements.instance.Instance;
import intellispaces.javastatements.method.MethodStatement;
import intellispaces.javastatements.reference.CustomTypeReference;
import intellispaces.javastatements.reference.TypeReference;

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
    addObjectHandleBunchGenerator(domainType, generators, roundEnv);
    addBasicObjectHandleGenerators(domainType, generators, roundEnv);
    addDownwardObjectHandleGenerators(domainType, generators);
    addUpwardObjectHandleGenerators(domainType, domainType, generators);
    addIncludedGenerators(domainType, generators, roundEnv);
    return generators;
  }

  private static void addIncludedGenerators(
      CustomType annotatedType, List<ArtifactGenerator> generators, RoundEnvironment roundEnv
  ) {
    List<AnnotatedTypeProcessor> processors = AnnotationFunctions.allAnnotationsOf(
        annotatedType, AnnotationProcessor.class
    ).stream()
        .map(AnnotationProcessor::value)
        .distinct()
        .map(c -> (AnnotatedTypeProcessor) TypeFunctions.newInstance(c))
        .toList();
    for (AnnotatedTypeProcessor processor : processors) {
      if (processor.isApplicable(annotatedType)) {
        processor.getValidator().validate(annotatedType);
        generators.addAll(processor.makeArtifactGenerators(annotatedType, roundEnv));
      }
    }
  }

  private static void addObjectHandleBunchGenerator(
      CustomType domainType, List<ArtifactGenerator> generators, RoundEnvironment roundEnv
  ) {
    if (isAutoGenerationEnabled(domainType, ArtifactTypes.ObjectHandleBranch, roundEnv)) {
      generators.add(new ObjectHandleBunchGenerator(domainType));
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

  private static void addDownwardObjectHandleGenerators(
      CustomType domainType, List<ArtifactGenerator> generators
  ) {
    List<CustomTypeReference> parents = domainType.parentTypes();
    if (parents.size() != 1) {
      return;
    }
    CustomTypeReference parentDomainType = parents.get(0);
    generators.add(new MovableDownwardObjectHandleGenerator(domainType, parentDomainType));
  }

  private static void addUpwardObjectHandleGenerators(
      CustomType domainType, CustomType curDomainType, List<ArtifactGenerator> generators
  ) {
    addUpwardObjectHandleGenerators2(domainType, curDomainType, new ArrayList<>(), generators);
  }

  private static void addUpwardObjectHandleGenerators2(
      CustomType domainType,
      CustomType curDomainType,
      List<CustomTypeReference> allPrimaryDomains,
      List<ArtifactGenerator> generators
  ) {
    Optional<CustomTypeReference> primaryDomain = DomainFunctions.getPrimaryDomainForAliasDomain(curDomainType);
    if (primaryDomain.isPresent()) {
      allPrimaryDomains.add(primaryDomain.get());
      generators.add(new UnmovableUpwardObjectHandleGenerator(domainType, primaryDomain.get(), List.copyOf(allPrimaryDomains)));
      addUpwardObjectHandleGenerators2(domainType, primaryDomain.get().targetType(), allPrimaryDomains, generators);
    }
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

  static List<ArtifactGenerator> makeMovableObjectHandleArtifactGenerators(CustomType objectHandleType) {
      return List.of(new MovableObjectHandleImplGenerator(objectHandleType));
  }

  static List<ArtifactGenerator> makeUnmovableObjectHandleArtifactGenerators(CustomType objectHandleType) {
    return List.of(new UnmovableObjectHandleImplGenerator(objectHandleType));
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
    List<CustomType> preprocessingClasses = getPreprocessingTargets(preprocessingAnnotation);
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
      } else if (preprocessingClass.hasAnnotation(MovableObjectHandle.class)) {
        generators.addAll(makeMovableObjectHandleArtifactGenerators(preprocessingClass));
      } else if (preprocessingClass.hasAnnotation(UnmovableObjectHandle.class)) {
        generators.addAll(makeUnmovableObjectHandleArtifactGenerators(preprocessingClass));
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
        .filter(ann -> isPreprocessingAnnotationFor(ann, annotatedType.canonicalName()))
        .toList();
    if (preprocessingAnnotations.isEmpty()) {
      return true;
    }
    return preprocessingAnnotations.stream().allMatch(AnnotationProcessorFunctions::isPreprocessingEnabled);
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

  static List<CustomType> findArtifactAnnexes(
      CustomType customType, ArtifactTypes artifactType, RoundEnvironment roundEnv
  ) {
    return findArtifactAnnexes(customType.canonicalName(), artifactType, roundEnv);
  }

  static List<CustomType> findArtifactAnnexes(
      String canonicalName, ArtifactTypes artifactType, RoundEnvironment roundEnv
  ) {
    return roundEnv.getElementsAnnotatedWith(Preprocessing.class).stream()
        .map(JavaStatements::statement)
        .map(stm -> (CustomType) stm)
        .filter(stm -> isArtifactAnnexFor(stm, canonicalName, artifactType))
        .toList();
  }

  static boolean isArtifactAnnexFor(
      CustomType customType, String canonicalName, ArtifactTypes artifactType
  ) {
    return isPreprocessingAnnotationFor(
        customType.selectAnnotation(Preprocessing.class.getCanonicalName()).orElseThrow(),
        canonicalName,
        artifactType
    );
  }

  static boolean isPreprocessingAnnotationFor(
      AnnotationInstance preprocessingAnnotation, String canonicalClassName
  ) {
    List<CustomType> preprocessingTargets = getPreprocessingAnnexTargets(preprocessingAnnotation);
    for (CustomType target : preprocessingTargets) {
      if (canonicalClassName.equals(target.canonicalName())) {
        return true;
      }
    }
    return false;
  }

  static boolean isPreprocessingAnnotationFor(
      AnnotationInstance preprocessingAnnotation, String canonicalClassName, ArtifactTypes artifact
  ) {
    List<CustomType> preprocessingTargets = getPreprocessingAnnexTargets(preprocessingAnnotation);
    for (CustomType target : preprocessingTargets) {
      if (canonicalClassName.equals(target.canonicalName())) {
        if (artifact.name().equals(getPreprocessingArtifactName(preprocessingAnnotation))) {
          return true;
        }
      }
    }
    return false;
  }

  static List<CustomType> getPreprocessingTargets(AnnotationInstance preprocessingAnnotation) {
    return preprocessingAnnotation.elementValue("value").orElseThrow()
        .asArray().orElseThrow()
        .elements().stream()
        .map(Instance::asClass)
        .map(Optional::orElseThrow)
        .map(ClassInstance::type)
        .toList();
  }

  static List<CustomType> getPreprocessingAnnexTargets(AnnotationInstance preprocessingAnnotation) {
    return preprocessingAnnotation.elementValue("annexFor").orElseThrow()
        .asArray().orElseThrow()
        .elements().stream()
        .map(Instance::asClass)
        .map(Optional::orElseThrow)
        .map(ClassInstance::type)
        .toList();
  }

  static boolean isPreprocessingEnabled(AnnotationInstance preprocessingAnnotation) {
    Object enabled = preprocessingAnnotation.elementValue("enable").orElseThrow()
        .asPrimitive().orElseThrow()
        .value();
    return Boolean.TRUE == enabled;
  }

  static String getPreprocessingArtifactName(AnnotationInstance preprocessingAnnotation) {
    return preprocessingAnnotation.elementValue("artifact").orElseThrow()
        .asString().orElseThrow()
        .value();
  }
}
