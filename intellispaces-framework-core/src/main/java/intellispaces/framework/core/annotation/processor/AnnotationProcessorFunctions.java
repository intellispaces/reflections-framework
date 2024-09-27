package intellispaces.framework.core.annotation.processor;

import intellispaces.common.annotationprocessor.AnnotatedTypeProcessor;
import intellispaces.common.annotationprocessor.generator.Generator;
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
import intellispaces.framework.core.annotation.processor.data.UnmovableDataHandleGenerator;
import intellispaces.framework.core.annotation.processor.domain.CommonObjectHandleGenerator;
import intellispaces.framework.core.annotation.processor.domain.DomainGuideGenerator;
import intellispaces.framework.core.annotation.processor.domain.DomainTransitionGenerator;
import intellispaces.framework.core.annotation.processor.domain.MovableDownwardObjectHandleGenerator;
import intellispaces.framework.core.annotation.processor.domain.MovableObjectHandleGenerator;
import intellispaces.framework.core.annotation.processor.domain.ObjectHandleBunchGenerator;
import intellispaces.framework.core.annotation.processor.domain.UnmovableObjectHandleGenerator;
import intellispaces.framework.core.annotation.processor.domain.UnmovableUpwardObjectHandleGenerator;
import intellispaces.framework.core.annotation.processor.guide.AutoGuideGenerator;
import intellispaces.framework.core.annotation.processor.objecthandle.MovableObjectHandleWrapperGenerator;
import intellispaces.framework.core.annotation.processor.objecthandle.UnmovableObjectHandleWrapperGenerator;
import intellispaces.framework.core.annotation.processor.ontology.OntologyGuideGenerator;
import intellispaces.framework.core.annotation.processor.ontology.OntologyTransitionGenerator;
import intellispaces.framework.core.annotation.processor.unit.UnitWrapperGenerator;
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

  static List<Generator> makeDataArtifactGenerators(CustomType initiatorType, CustomType dataType) {
    List<Generator> generators = new ArrayList<>();
    generators.add(new UnmovableDataHandleGenerator(initiatorType, dataType));
    return generators;
  }

  static List<Generator> makeDomainArtifactGenerators(
      CustomType initiatorType, CustomType domainType, RoundEnvironment roundEnv
  ) {
    List<Generator> generators = new ArrayList<>();
    for (MethodStatement method : domainType.declaredMethods()) {
      if (method.hasAnnotation(Transition.class)) {
        if (isAutoGenerationEnabled(domainType, ArtifactTypes.Transition, roundEnv)) {
          generators.add(new DomainTransitionGenerator(initiatorType, domainType, method));
        }
        if (isEnableMapperGuideGeneration(domainType, method, roundEnv)) {
          generators.add(new DomainGuideGenerator(TraverseTypes.Mapping, initiatorType, domainType, method));
        }
        if (isEnableMoverGuideGeneration(domainType, method, roundEnv)) {
          generators.add(new DomainGuideGenerator(TraverseTypes.Moving, initiatorType, domainType, method));
        }
        if (isEnableMapperOfMovingGuideGeneration(domainType, method, roundEnv)) {
          generators.add(new DomainGuideGenerator(TraverseTypes.MappingOfMoving, initiatorType, domainType, method));
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
      CustomType initiatorType, CustomType annotatedType, List<Generator> generators, RoundEnvironment roundEnv
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
        generators.addAll(processor.makeGenerators(initiatorType, annotatedType, roundEnv));
      }
    }
  }

  private static void addObjectHandleBunchGenerator(
      CustomType initiatorType, CustomType domainType, List<Generator> generators, RoundEnvironment roundEnv
  ) {
    if (isAutoGenerationEnabled(domainType, ArtifactTypes.ObjectHandleBranch, roundEnv)) {
      generators.add(new ObjectHandleBunchGenerator(initiatorType, domainType));
    }
  }

  private static void addBasicObjectHandleGenerators(
      CustomType initiatorType, CustomType domainType, List<Generator> generators, RoundEnvironment roundEnv
  ) {
    if (isAutoGenerationEnabled(domainType, ArtifactTypes.ObjectHandle, roundEnv)) {
      generators.add(new CommonObjectHandleGenerator(initiatorType, domainType));
    }
    if (isAutoGenerationEnabled(domainType, ArtifactTypes.MovableObjectHandle, roundEnv)) {
      generators.add(new MovableObjectHandleGenerator(initiatorType, domainType));
    }
    if (isAutoGenerationEnabled(domainType, ArtifactTypes.UnmovableObjectHandle, roundEnv)) {
      generators.add(new UnmovableObjectHandleGenerator(initiatorType, domainType));
    }
  }

  private static void addDownwardObjectHandleGenerators(
      CustomType initiatorType, CustomType domainType, List<Generator> generators
  ) {
    List<CustomTypeReference> parents = domainType.parentTypes();
    if (parents.size() != 1) {
      return;
    }
    CustomTypeReference parentDomainType = parents.get(0);
    generators.add(new MovableDownwardObjectHandleGenerator(initiatorType, domainType, parentDomainType));
  }

  private static void addUpwardObjectHandleGenerators(
      CustomType initiatorType, CustomType domainType, CustomType curDomainType, List<Generator> generators
  ) {
    addUpwardObjectHandleGenerators2(initiatorType, domainType, curDomainType, new ArrayList<>(), generators);
  }

  private static void addUpwardObjectHandleGenerators2(
      CustomType initiatorType,
      CustomType domainType,
      CustomType curDomainType,
      List<CustomTypeReference> allPrimaryDomains,
      List<Generator> generators
  ) {
    Optional<CustomTypeReference> primaryDomain = DomainFunctions.getPrimaryDomainForAliasDomain(curDomainType);
    if (primaryDomain.isPresent()) {
      allPrimaryDomains.add(primaryDomain.get());
      generators.add(new UnmovableUpwardObjectHandleGenerator(
          initiatorType, domainType, primaryDomain.get(), List.copyOf(allPrimaryDomains))
      );
      addUpwardObjectHandleGenerators2(
          initiatorType, domainType, primaryDomain.get().targetType(), allPrimaryDomains, generators
      );
    }
  }

  static List<Generator> makeOntologyArtifactGenerators(
      CustomType initiatorType, CustomType ontologyType, RoundEnvironment roundEnv
  ) {
    List<Generator> generators = new ArrayList<>();
    for (MethodStatement method : ontologyType.declaredMethods()) {
      if (method.hasAnnotation(Transition.class)) {
        if (isAutoGenerationEnabled(ontologyType, ArtifactTypes.Transition, roundEnv)) {
          generators.add(new OntologyTransitionGenerator(initiatorType, ontologyType, method));
        }
        if (isEnableMapperGuideGeneration(ontologyType, method, roundEnv)) {
          generators.add(new OntologyGuideGenerator(TraverseTypes.Mapping, initiatorType, ontologyType, method));
        }
        if (isEnableMoverGuideGeneration(ontologyType, method, roundEnv)) {
          generators.add(new OntologyGuideGenerator(TraverseTypes.Moving, initiatorType, ontologyType, method));
        }
      }
    }
    return generators;
  }

  static List<Generator> makeObjectHandleArtifactGenerators(
      CustomType initiatorType, CustomType objectHandleType
  ) {
    if (objectHandleType.hasParent(UnmovableObjectHandle.class)) {
      return List.of(new UnmovableObjectHandleWrapperGenerator(initiatorType, objectHandleType));
    } else if (objectHandleType.hasParent(MovableObjectHandle.class)) {
      return List.of(new MovableObjectHandleWrapperGenerator(initiatorType, objectHandleType));
    } else {
      throw UnexpectedViolationException.withMessage("Could not define movable type of the object handle {0}",
          objectHandleType.canonicalName());
    }
  }

  static List<Generator> makeModuleArtifactGenerators(CustomType initiatorType, CustomType moduleType) {
    List<Generator> generators = new ArrayList<>();
    generators.add(new UnitWrapperGenerator(initiatorType, moduleType));
    Iterable<CustomType> includedUnits = ModuleFunctions.getIncludedUnits(moduleType);
    includedUnits.forEach(u -> generators.add(new UnitWrapperGenerator(initiatorType, u)));
    return generators;
  }

  static List<Generator> makePreprocessingArtifactGenerators(
      CustomType initiatorType, CustomType customType, RoundEnvironment roundEnv
  ) {
    AnnotationInstance preprocessingAnnotation = customType.selectAnnotation(
        Preprocessing.class.getCanonicalName()).orElseThrow();
    List<CustomType> preprocessingClasses = getPreprocessingTargets(preprocessingAnnotation);
    if (preprocessingClasses.isEmpty()) {
      return List.of();
    }

    List<Generator> generators = new ArrayList<>();
    for (CustomType preprocessingClass : preprocessingClasses) {
      if (preprocessingClass.hasAnnotation(Data.class)) {
        generators.addAll(makeDataArtifactGenerators(initiatorType, preprocessingClass));
      } else if (preprocessingClass.hasAnnotation(Domain.class)) {
        generators.addAll(makeDomainArtifactGenerators(initiatorType, preprocessingClass, roundEnv));
      } else  if (preprocessingClass.hasAnnotation(Module.class)) {
        generators.addAll(makeModuleArtifactGenerators(initiatorType, preprocessingClass));
      } else if (UnitFunctions.isUnitType(preprocessingClass)) {
        generators.add(new UnitWrapperGenerator(initiatorType, preprocessingClass));
      } else if (UnitFunctions.isGuideInterface(preprocessingClass)) {
        generators.add(new AutoGuideGenerator(initiatorType, preprocessingClass));
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
        ArraysFunctions.contains(method.selectAnnotation(Transition.class).orElseThrow().allowedTraverse(),
            TraverseTypes.Moving);
  }

  static boolean isEnableMapperOfMovingGuideGeneration(
      CustomType domainType, MethodStatement method, RoundEnvironment roundEnv
  ) {
    return isAutoGenerationEnabled(domainType, ArtifactTypes.MapperOfMoving, roundEnv) &&
        ArraysFunctions.contains(method.selectAnnotation(Transition.class).orElseThrow().allowedTraverse(),
            TraverseTypes.MappingOfMoving);
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
    List<CustomType> preprocessingTargets = getPreprocessingAddOnTargets(preprocessingAnnotation);
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
    List<CustomType> preprocessingTargets = getPreprocessingAddOnTargets(preprocessingAnnotation);
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

  static List<CustomType> getPreprocessingAddOnTargets(AnnotationInstance preprocessingAnnotation) {
    return preprocessingAnnotation.elementValue("addOnFor").orElseThrow()
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
