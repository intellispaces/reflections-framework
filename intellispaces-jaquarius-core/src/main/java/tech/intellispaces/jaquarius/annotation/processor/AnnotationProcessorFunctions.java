package tech.intellispaces.jaquarius.annotation.processor;

import tech.intellispaces.jaquarius.annotation.AnnotationProcessor;
import tech.intellispaces.jaquarius.annotation.Channel;
import tech.intellispaces.jaquarius.annotation.Data;
import tech.intellispaces.jaquarius.annotation.Domain;
import tech.intellispaces.jaquarius.annotation.Module;
import tech.intellispaces.jaquarius.annotation.ObjectHandle;
import tech.intellispaces.jaquarius.annotation.Ontology;
import tech.intellispaces.jaquarius.annotation.Preprocessing;
import tech.intellispaces.jaquarius.annotation.processor.channel.ChannelGuideGenerator;
import tech.intellispaces.jaquarius.annotation.processor.data.UnmovableDataHandleGenerator;
import tech.intellispaces.jaquarius.annotation.processor.domain.CommonObjectHandleGenerator;
import tech.intellispaces.jaquarius.annotation.processor.domain.DomainChannelGenerator;
import tech.intellispaces.jaquarius.annotation.processor.domain.MovableDownwardObjectHandleGenerator;
import tech.intellispaces.jaquarius.annotation.processor.domain.MovableObjectHandleGenerator;
import tech.intellispaces.jaquarius.annotation.processor.domain.UnmovableDownwardObjectHandleGenerator;
import tech.intellispaces.jaquarius.annotation.processor.domain.UnmovableObjectHandleGenerator;
import tech.intellispaces.jaquarius.annotation.processor.guide.AutoGuideGenerator;
import tech.intellispaces.jaquarius.annotation.processor.objecthandle.MovableObjectHandleWrapperGenerator;
import tech.intellispaces.jaquarius.annotation.processor.objecthandle.UnmovableObjectHandleWrapperGenerator;
import tech.intellispaces.jaquarius.annotation.processor.ontology.OntologyChannelGenerator;
import tech.intellispaces.jaquarius.annotation.processor.unit.UnitWrapperGenerator;
import tech.intellispaces.jaquarius.channel.MappingChannel;
import tech.intellispaces.jaquarius.channel.MappingOfMovingChannel;
import tech.intellispaces.jaquarius.channel.MovingChannel;
import tech.intellispaces.jaquarius.exception.ConfigurationExceptions;
import tech.intellispaces.jaquarius.object.ObjectHandleFunctions;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForm;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForms;
import tech.intellispaces.jaquarius.system.ModuleFunctions;
import tech.intellispaces.jaquarius.system.UnitFunctions;
import tech.intellispaces.jaquarius.traverse.TraverseType;
import tech.intellispaces.jaquarius.traverse.TraverseTypes;
import tech.intellispaces.entity.collection.ArraysFunctions;
import tech.intellispaces.entity.exception.UnexpectedExceptions;
import tech.intellispaces.entity.type.ClassFunctions;
import tech.intellispaces.java.annotation.AnnotatedTypeProcessor;
import tech.intellispaces.java.annotation.generator.Generator;
import tech.intellispaces.java.annotation.validator.AnnotatedTypeValidator;
import tech.intellispaces.java.reflection.AnnotatedStatement;
import tech.intellispaces.java.reflection.JavaStatements;
import tech.intellispaces.java.reflection.customtype.AnnotationFunctions;
import tech.intellispaces.java.reflection.customtype.CustomType;
import tech.intellispaces.java.reflection.instance.AnnotationInstance;
import tech.intellispaces.java.reflection.method.MethodStatement;
import tech.intellispaces.java.reflection.reference.CustomTypeReference;
import tech.intellispaces.java.reflection.reference.TypeReference;
import tech.intellispaces.java.reflection.instance.Instance;
import tech.intellispaces.java.reflection.instance.ClassInstance;

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

  static List<Generator> makeChannelArtifactGenerators(
      CustomType initiatorType, CustomType channelType, RoundEnvironment roundEnv
  ) {
    List<Generator> generators = new ArrayList<>();

    List<MethodStatement> methods = channelType.declaredMethods();
    if (methods.size() != 1) {
      throw ConfigurationExceptions.withMessage("Channel class should contain one method only. Check class {0}",
          channelType.canonicalName());
    }
    MethodStatement method = methods.get(0);

    if (isEnableMapperGuideGeneration(channelType, roundEnv)) {
      generators.addAll(makeGuideGenerators(TraverseTypes.Mapping, initiatorType, channelType, method));
    }
    if (isEnableMoverGuideGeneration(channelType, roundEnv)) {
      generators.addAll(makeGuideGenerators(TraverseTypes.Moving, initiatorType, channelType, method));
    }
    if (isEnableMapperOfMovingGuideGeneration(channelType, roundEnv)) {
      generators.addAll(makeGuideGenerators(TraverseTypes.MappingOfMoving, initiatorType, channelType, method));
    }
    return generators;
  }

  static List<Generator> makeDomainArtifactGenerators(
      CustomType initiatorType, CustomType domainType, RoundEnvironment roundEnv
  ) {
    List<Generator> generators = new ArrayList<>();
    for (MethodStatement method : domainType.declaredMethods()) {
      if (method.hasAnnotation(Channel.class)) {
        if (isAutoGenerationEnabled(domainType, ArtifactTypes.Channel, roundEnv)) {
          generators.add(new DomainChannelGenerator(initiatorType, domainType, method));
        }
      }
    }
    addBasicObjectHandleGenerators(initiatorType, domainType, generators, roundEnv);
    addDownwardObjectHandleGenerators(initiatorType, domainType, generators);
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
        .map(c -> (AnnotatedTypeProcessor) tech.intellispaces.entity.object.ObjectFunctions.newInstance(c))
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
    generators.add(new UnmovableDownwardObjectHandleGenerator(initiatorType, domainType, parentDomainType));
    generators.add(new MovableDownwardObjectHandleGenerator(initiatorType, domainType, parentDomainType));
  }

  static List<Generator> makeOntologyArtifactGenerators(
      CustomType initiatorType, CustomType ontologyType, RoundEnvironment roundEnv
  ) {
    List<Generator> generators = new ArrayList<>();
    for (MethodStatement method : ontologyType.declaredMethods()) {
      if (method.hasAnnotation(Channel.class)) {
        if (isAutoGenerationEnabled(ontologyType, ArtifactTypes.Channel, roundEnv)) {
          generators.add(new OntologyChannelGenerator(initiatorType, ontologyType, method));
        }
      }
    }
    return generators;
  }

  private static List<Generator> makeGuideGenerators(
      TraverseType traverseType,
      CustomType initiatorType,
      CustomType domainType,
      MethodStatement channelMethod
  ) {
    List<Generator> generators = new ArrayList<>();
    generators.add(
        makeGuideGenerator(ObjectReferenceForms.Object, traverseType, initiatorType, domainType, channelMethod)
    );
    if (channelMethod.returnType().isPresent()) {
      TypeReference returnType = channelMethod.returnType().get();
      if (returnType.isCustomTypeReference()) {
        CustomTypeReference customTypeReference = returnType.asCustomTypeReferenceOrElseThrow();
        if (ClassFunctions.isPrimitiveWrapperClass(customTypeReference.targetType().canonicalName())) {
          generators.add(makeGuideGenerator(
              ObjectReferenceForms.Primitive, traverseType, initiatorType, domainType, channelMethod
          ));
        }
      }
    }
    return generators;
  }

  private static Generator makeGuideGenerator(
      ObjectReferenceForm targetForm,
      TraverseType traverseType,
      CustomType initiatorType,
      CustomType domainType,
      MethodStatement channelMethod
  ) {
    if (initiatorType.hasAnnotation(Channel.class)) {
      return new ChannelGuideGenerator(targetForm, traverseType, initiatorType, domainType, channelMethod);
    } else {
      throw new RuntimeException();
    }
  }

  static List<Generator> makeObjectHandleArtifactGenerators(
      CustomType initiatorType, CustomType objectHandleType
  ) {
    if (ObjectHandleFunctions.isUnmovableObjectHandle(objectHandleType)) {
      return List.of(new UnmovableObjectHandleWrapperGenerator(initiatorType, objectHandleType));
    } else if (ObjectHandleFunctions.isMovableObjectHandle(objectHandleType)) {
      return List.of(new MovableObjectHandleWrapperGenerator(initiatorType, objectHandleType));
    } else {
      throw UnexpectedExceptions.withMessage("Could not define movable type of the object handle {0}",
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
        ArraysFunctions.contains(method.selectAnnotation(Channel.class).orElseThrow().allowedTraverse(),
            TraverseTypes.Mapping);
  }

  static boolean isEnableMoverGuideGeneration(
      CustomType domainType, MethodStatement method, RoundEnvironment roundEnv
  ) {
    return isAutoGenerationEnabled(domainType, ArtifactTypes.Mover, roundEnv) &&
        ArraysFunctions.contains(method.selectAnnotation(Channel.class).orElseThrow().allowedTraverse(),
            TraverseTypes.Moving);
  }

  static boolean isEnableMapperOfMovingGuideGeneration(
      CustomType domainType, MethodStatement method, RoundEnvironment roundEnv
  ) {
    return isAutoGenerationEnabled(domainType, ArtifactTypes.MapperOfMoving, roundEnv) &&
        ArraysFunctions.contains(method.selectAnnotation(Channel.class).orElseThrow().allowedTraverse(),
            TraverseTypes.MappingOfMoving);
  }

  static boolean isEnableMapperGuideGeneration(CustomType channelType, RoundEnvironment roundEnv) {
    return channelType.hasParent(MappingChannel.class);
  }

  static boolean isEnableMoverGuideGeneration(CustomType channelType, RoundEnvironment roundEnv) {
    return channelType.hasParent(MovingChannel.class);
  }

  static boolean isEnableMapperOfMovingGuideGeneration(CustomType channelType, RoundEnvironment roundEnv) {
    return channelType.hasParent(MappingOfMovingChannel.class);
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
          ClassFunctions.getPrimitiveWrapperClass(type.asPrimitiveReferenceOrElseThrow().typename()).getSimpleName() +
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

  static List<CustomType> findArtifactAddOns(
      CustomType customType, ArtifactTypes artifactType, RoundEnvironment roundEnv
  ) {
    return findArtifactAddOns(customType.canonicalName(), artifactType, roundEnv);
  }

  static List<CustomType> findArtifactAddOns(
      String canonicalName, ArtifactTypes artifactType, RoundEnvironment roundEnv
  ) {
    return roundEnv.getElementsAnnotatedWith(Preprocessing.class).stream()
        .map(JavaStatements::statement)
        .map(stm -> (CustomType) stm)
        .filter(stm -> isArtifactAddOnFor(stm, canonicalName, artifactType))
        .toList();
  }

  static boolean isArtifactAddOnFor(
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
    return preprocessingAnnotation.value().orElseThrow()
        .asArray().orElseThrow()
        .elements().stream()
        .map(Instance::asClass)
        .map(Optional::orElseThrow)
        .map(ClassInstance::type)
        .toList();
  }

  static List<CustomType> getPreprocessingAddOnTargets(AnnotationInstance preprocessingAnnotation) {
    return preprocessingAnnotation.valueOf("addOnFor").orElseThrow()
        .asArray().orElseThrow()
        .elements().stream()
        .map(Instance::asClass)
        .map(Optional::orElseThrow)
        .map(ClassInstance::type)
        .toList();
  }

  static boolean isPreprocessingEnabled(AnnotationInstance preprocessingAnnotation) {
    Object enabled = preprocessingAnnotation.valueOf("enable").orElseThrow()
        .asPrimitive().orElseThrow()
        .value();
    return Boolean.TRUE == enabled;
  }

  static String getPreprocessingArtifactName(AnnotationInstance preprocessingAnnotation) {
    return preprocessingAnnotation.valueOf("artifact").orElseThrow()
        .asString().orElseThrow()
        .value();
  }
}
