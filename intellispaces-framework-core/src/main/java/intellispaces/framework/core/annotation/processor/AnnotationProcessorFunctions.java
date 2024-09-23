package intellispaces.framework.core.annotation.processor;

import intellispaces.common.annotationprocessor.AnnotatedTypeProcessor;
import intellispaces.common.annotationprocessor.generator.GenerationTask;
import intellispaces.common.annotationprocessor.validator.AnnotatedTypeValidator;
import intellispaces.common.base.collection.ArraysFunctions;
import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.common.base.type.TypeFunctions;
import intellispaces.common.javastatement.AnnotatedStatement;
import intellispaces.common.javastatement.JavaStatements;
import intellispaces.common.javastatement.customtype.AnnotationFunctions;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.common.javastatement.instance.AnnotationInstance;
import intellispaces.common.javastatement.instance.ClassInstance;
import intellispaces.common.javastatement.instance.Instance;
import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.common.javastatement.reference.CustomTypeReference;
import intellispaces.common.javastatement.reference.TypeReference;
import intellispaces.framework.core.annotation.AnnotationProcessor;
import intellispaces.framework.core.annotation.Data;
import intellispaces.framework.core.annotation.Domain;
import intellispaces.framework.core.annotation.Module;
import intellispaces.framework.core.annotation.ObjectHandle;
import intellispaces.framework.core.annotation.Ontology;
import intellispaces.framework.core.annotation.Preprocessing;
import intellispaces.framework.core.annotation.Transition;
import intellispaces.framework.core.annotation.processor.data.UnmovableDataHandleGenerationTask;
import intellispaces.framework.core.annotation.processor.domain.CommonObjectHandleGenerationTask;
import intellispaces.framework.core.annotation.processor.domain.DomainGuideGenerationTask;
import intellispaces.framework.core.annotation.processor.domain.DomainTransitionGenerationTask;
import intellispaces.framework.core.annotation.processor.domain.MovableDownwardObjectHandleGenerationTask;
import intellispaces.framework.core.annotation.processor.domain.MovableObjectHandleGenerationTask;
import intellispaces.framework.core.annotation.processor.domain.ObjectHandleBunchGenerationTask;
import intellispaces.framework.core.annotation.processor.domain.UnmovableObjectHandleGenerationTask;
import intellispaces.framework.core.annotation.processor.domain.UnmovableUpwardObjectHandleGenerationTask;
import intellispaces.framework.core.annotation.processor.guide.AutoGuideGenerationTask;
import intellispaces.framework.core.annotation.processor.objecthandle.MovableObjectHandleWrapperGenerationTask;
import intellispaces.framework.core.annotation.processor.objecthandle.UnmovableObjectHandleWrapperGenerationTask;
import intellispaces.framework.core.annotation.processor.ontology.OntologyGuideGenerationTask;
import intellispaces.framework.core.annotation.processor.ontology.OntologyTransitionGenerationTask;
import intellispaces.framework.core.annotation.processor.unit.UnitWrapperGenerationTask;
import intellispaces.framework.core.object.MovableObjectHandle;
import intellispaces.framework.core.object.UnmovableObjectHandle;
import intellispaces.framework.core.space.domain.DomainFunctions;
import intellispaces.framework.core.system.ModuleFunctions;
import intellispaces.framework.core.system.UnitFunctions;
import intellispaces.framework.core.traverse.TraverseTypes;

import javax.annotation.processing.RoundEnvironment;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface AnnotationProcessorFunctions {

  static List<GenerationTask> makeDataArtifactGenerators(CustomType initiatorType, CustomType dataType) {
    List<GenerationTask> generators = new ArrayList<>();
    generators.add(new UnmovableDataHandleGenerationTask(initiatorType, dataType));
    return generators;
  }

  static List<GenerationTask> makeDomainArtifactGenerators(
      CustomType initiatorType, CustomType domainType, RoundEnvironment roundEnv
  ) {
    List<GenerationTask> generators = new ArrayList<>();
    for (MethodStatement method : domainType.declaredMethods()) {
      if (method.hasAnnotation(Transition.class)) {
        if (isAutoGenerationEnabled(domainType, ArtifactTypes.Transition, roundEnv)) {
          generators.add(new DomainTransitionGenerationTask(initiatorType, domainType, method));
        }
        if (isEnableMapperGuideGeneration(domainType, method, roundEnv)) {
          generators.add(new DomainGuideGenerationTask(TraverseTypes.Mapping, initiatorType, domainType, method));
        }
        if (isEnableMoverGuideGeneration(domainType, method, roundEnv)) {
          generators.add(new DomainGuideGenerationTask(TraverseTypes.Moving, initiatorType, domainType, method));
        }
      }
    }
    addObjectHandleBunchGenerator(initiatorType, domainType, generators, roundEnv);
    addBasicObjectHandleGenerators(initiatorType, domainType, generators, roundEnv);
    addDownwardObjectHandleGenerators(initiatorType, domainType, generators);
    addUpwardObjectHandleGenerators(initiatorType, domainType, domainType, generators);
    addIncludedGenerators(initiatorType, domainType, generators, roundEnv);
    return generators;
  }

  private static void addIncludedGenerators(
      CustomType initiatorType, CustomType annotatedType, List<GenerationTask> generators, RoundEnvironment roundEnv
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
        AnnotatedTypeValidator validator = processor.getValidator();
        if (validator != null) {
          validator.validate(annotatedType);
        }
        generators.addAll(processor.makeTasks(initiatorType, annotatedType, roundEnv));
      }
    }
  }

  private static void addObjectHandleBunchGenerator(
      CustomType initiatorType, CustomType domainType, List<GenerationTask> generators, RoundEnvironment roundEnv
  ) {
    if (isAutoGenerationEnabled(domainType, ArtifactTypes.ObjectHandleBranch, roundEnv)) {
      generators.add(new ObjectHandleBunchGenerationTask(initiatorType, domainType));
    }
  }

  private static void addBasicObjectHandleGenerators(
      CustomType initiatorType, CustomType domainType, List<GenerationTask> generators, RoundEnvironment roundEnv
  ) {
    if (isAutoGenerationEnabled(domainType, ArtifactTypes.ObjectHandle, roundEnv)) {
      generators.add(new CommonObjectHandleGenerationTask(initiatorType, domainType));
    }
    if (isAutoGenerationEnabled(domainType, ArtifactTypes.MovableObjectHandle, roundEnv)) {
      generators.add(new MovableObjectHandleGenerationTask(initiatorType, domainType));
    }
    if (isAutoGenerationEnabled(domainType, ArtifactTypes.UnmovableObjectHandle, roundEnv)) {
      generators.add(new UnmovableObjectHandleGenerationTask(initiatorType, domainType));
    }
  }

  private static void addDownwardObjectHandleGenerators(
      CustomType initiatorType, CustomType domainType, List<GenerationTask> generators
  ) {
    List<CustomTypeReference> parents = domainType.parentTypes();
    if (parents.size() != 1) {
      return;
    }
    CustomTypeReference parentDomainType = parents.get(0);
    generators.add(new MovableDownwardObjectHandleGenerationTask(initiatorType, domainType, parentDomainType));
  }

  private static void addUpwardObjectHandleGenerators(
      CustomType initiatorType, CustomType domainType, CustomType curDomainType, List<GenerationTask> generators
  ) {
    addUpwardObjectHandleGenerators2(initiatorType, domainType, curDomainType, new ArrayList<>(), generators);
  }

  private static void addUpwardObjectHandleGenerators2(
      CustomType initiatorType,
      CustomType domainType,
      CustomType curDomainType,
      List<CustomTypeReference> allPrimaryDomains,
      List<GenerationTask> generators
  ) {
    Optional<CustomTypeReference> primaryDomain = DomainFunctions.getPrimaryDomainForAliasDomain(curDomainType);
    if (primaryDomain.isPresent()) {
      allPrimaryDomains.add(primaryDomain.get());
      generators.add(new UnmovableUpwardObjectHandleGenerationTask(
          initiatorType, domainType, primaryDomain.get(), List.copyOf(allPrimaryDomains))
      );
      addUpwardObjectHandleGenerators2(
          initiatorType, domainType, primaryDomain.get().targetType(), allPrimaryDomains, generators
      );
    }
  }

  static List<GenerationTask> makeOntologyArtifactGenerators(
      CustomType initiatorType, CustomType ontologyType, RoundEnvironment roundEnv
  ) {
    List<GenerationTask> generators = new ArrayList<>();
    for (MethodStatement method : ontologyType.declaredMethods()) {
      if (method.hasAnnotation(Transition.class)) {
        if (isAutoGenerationEnabled(ontologyType, ArtifactTypes.Transition, roundEnv)) {
          generators.add(new OntologyTransitionGenerationTask(initiatorType, ontologyType, method));
        }
        if (isEnableMapperGuideGeneration(ontologyType, method, roundEnv)) {
          generators.add(new OntologyGuideGenerationTask(TraverseTypes.Mapping, initiatorType, ontologyType, method));
        }
        if (isEnableMoverGuideGeneration(ontologyType, method, roundEnv)) {
          generators.add(new OntologyGuideGenerationTask(TraverseTypes.Moving, initiatorType, ontologyType, method));
        }
      }
    }
    return generators;
  }

  static List<GenerationTask> makeObjectHandleArtifactGenerators(
      CustomType initiatorType, CustomType objectHandleType
  ) {
    if (objectHandleType.hasParent(UnmovableObjectHandle.class)) {
      return List.of(new UnmovableObjectHandleWrapperGenerationTask(initiatorType, objectHandleType));
    } else if (objectHandleType.hasParent(MovableObjectHandle.class)) {
      return List.of(new MovableObjectHandleWrapperGenerationTask(initiatorType, objectHandleType));
    } else {
      throw UnexpectedViolationException.withMessage("Could not define movable type of the object handle {0}",
          objectHandleType.canonicalName());
    }
  }

  static List<GenerationTask> makeModuleArtifactGenerators(CustomType initiatorType, CustomType moduleType) {
    List<GenerationTask> generators = new ArrayList<>();
    generators.add(new UnitWrapperGenerationTask(initiatorType, moduleType));
    Iterable<CustomType> includedUnits = ModuleFunctions.getIncludedUnits(moduleType);
    includedUnits.forEach(u -> generators.add(new UnitWrapperGenerationTask(initiatorType, u)));
    return generators;
  }

  static List<GenerationTask> makePreprocessingArtifactGenerators(
      CustomType initiatorType, CustomType customType, RoundEnvironment roundEnv
  ) {
    AnnotationInstance preprocessingAnnotation = customType.selectAnnotation(
        Preprocessing.class.getCanonicalName()).orElseThrow();
    List<CustomType> preprocessingClasses = getPreprocessingTargets(preprocessingAnnotation);
    if (preprocessingClasses.isEmpty()) {
      return List.of();
    }

    List<GenerationTask> generators = new ArrayList<>();
    for (CustomType preprocessingClass : preprocessingClasses) {
      if (preprocessingClass.hasAnnotation(Data.class)) {
        generators.addAll(makeDataArtifactGenerators(initiatorType, preprocessingClass));
      } else if (preprocessingClass.hasAnnotation(Domain.class)) {
        generators.addAll(makeDomainArtifactGenerators(initiatorType, preprocessingClass, roundEnv));
      } else  if (preprocessingClass.hasAnnotation(Module.class)) {
        generators.addAll(makeModuleArtifactGenerators(initiatorType, preprocessingClass));
      } else if (UnitFunctions.isUnitType(preprocessingClass)) {
        generators.add(new UnitWrapperGenerationTask(initiatorType, preprocessingClass));
      } else if (UnitFunctions.isGuideInterface(preprocessingClass)) {
        generators.add(new AutoGuideGenerationTask(initiatorType, preprocessingClass));
      } else if (preprocessingClass.hasAnnotation(ObjectHandle.class)) {
        if (preprocessingClass.asClass().isPresent()) {
          generators.addAll(makeObjectHandleArtifactGenerators(initiatorType, preprocessingClass));
        }
      } else if (preprocessingClass.hasAnnotation(Ontology.class)) {
        generators.addAll(makeOntologyArtifactGenerators(initiatorType, preprocessingClass, roundEnv));
      }
    }
    return generators;
  }

  static boolean isEnableMapperGuideGeneration(
      CustomType domainType, MethodStatement method, RoundEnvironment roundEnv
  ) {
    return isAutoGenerationEnabled(domainType, ArtifactTypes.Mapper, roundEnv) &&
        ArraysFunctions.contains(method.selectAnnotation(Transition.class).orElseThrow().allowedTraverse(),
            TraverseTypes.Mapping);
  }

  static boolean isEnableMoverGuideGeneration(
      CustomType domainType, MethodStatement method, RoundEnvironment roundEnv
  ) {
    return isAutoGenerationEnabled(domainType, ArtifactTypes.Mover, roundEnv) &&
        ArraysFunctions.containsAny(method.selectAnnotation(Transition.class).orElseThrow().allowedTraverse(),
            TraverseTypes.Moving, TraverseTypes.MappingRelatedToMoving);
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
    List<CustomType> preprocessingTargets = getPreprocessingAddOnsTargets(preprocessingAnnotation);
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
    List<CustomType> preprocessingTargets = getPreprocessingAddOnsTargets(preprocessingAnnotation);
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

  static List<CustomType> getPreprocessingAddOnsTargets(AnnotationInstance preprocessingAnnotation) {
    return preprocessingAnnotation.elementValue("addOnsTo").orElseThrow()
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
