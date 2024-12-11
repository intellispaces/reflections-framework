package tech.intellispaces.jaquarius.annotationprocessor;

import tech.intellispaces.annotationprocessor.ArtifactGenerator;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.annotationprocessor.ArtifactProcessor;
import tech.intellispaces.annotationprocessor.ArtifactValidator;
import tech.intellispaces.general.collection.ArraysFunctions;
import tech.intellispaces.general.exception.UnexpectedExceptions;
import tech.intellispaces.general.object.Objects;
import tech.intellispaces.general.text.StringFunctions;
import tech.intellispaces.general.type.ClassFunctions;
import tech.intellispaces.general.type.Classes;
import tech.intellispaces.jaquarius.annotation.AnnotationProcessor;
import tech.intellispaces.jaquarius.annotation.Channel;
import tech.intellispaces.jaquarius.annotation.Data;
import tech.intellispaces.jaquarius.annotation.Domain;
import tech.intellispaces.jaquarius.annotation.Module;
import tech.intellispaces.jaquarius.annotation.ObjectHandle;
import tech.intellispaces.jaquarius.annotation.Ontology;
import tech.intellispaces.jaquarius.annotation.Preprocessing;
import tech.intellispaces.jaquarius.annotationprocessor.channel.GuideGenerator;
import tech.intellispaces.jaquarius.annotationprocessor.data.UnmovableDataHandleGenerator;
import tech.intellispaces.jaquarius.annotationprocessor.domain.CommonObjectHandleGenerator;
import tech.intellispaces.jaquarius.annotationprocessor.domain.ChannelGenerator;
import tech.intellispaces.jaquarius.annotationprocessor.domain.MovableDownwardObjectHandleGenerator;
import tech.intellispaces.jaquarius.annotationprocessor.domain.MovableObjectHandleGenerator;
import tech.intellispaces.jaquarius.annotationprocessor.domain.UnmovableDownwardObjectHandleGenerator;
import tech.intellispaces.jaquarius.annotationprocessor.domain.UnmovableObjectHandleGenerator;
import tech.intellispaces.jaquarius.annotationprocessor.guide.AutoGuideGenerator;
import tech.intellispaces.jaquarius.annotationprocessor.objecthandle.MovableObjectHandleWrapperGenerator;
import tech.intellispaces.jaquarius.annotationprocessor.objecthandle.UnmovableObjectHandleWrapperGenerator;
import tech.intellispaces.jaquarius.annotationprocessor.unit.UnitWrapperGenerator;
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
import tech.intellispaces.java.reflection.AnnotatedStatement;
import tech.intellispaces.java.reflection.JavaStatements;
import tech.intellispaces.java.reflection.customtype.AnnotationFunctions;
import tech.intellispaces.java.reflection.customtype.CustomType;
import tech.intellispaces.java.reflection.instance.AnnotationInstance;
import tech.intellispaces.java.reflection.instance.ClassInstance;
import tech.intellispaces.java.reflection.instance.Instance;
import tech.intellispaces.java.reflection.method.MethodStatement;
import tech.intellispaces.java.reflection.reference.CustomTypeReference;
import tech.intellispaces.java.reflection.reference.TypeReference;

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

  static List<ArtifactGenerator> makeChannelArtifactGenerators(CustomType channelType) {
    List<MethodStatement> methods = channelType.declaredMethods();
    if (methods.size() != 1) {
      throw ConfigurationExceptions.withMessage("Channel class should contain one method only. Check class {0}",
          channelType.canonicalName());
    }
    MethodStatement method = methods.get(0);

    List<ArtifactGenerator> generators = new ArrayList<>();
    if (isEnableMapperGuideGeneration(channelType)) {
      generators.addAll(
          makeGuideArtifactGenerators(TraverseTypes.Mapping, channelType, method));
    }
    if (isEnableMoverGuideGeneration(channelType)) {
      generators.addAll(
          makeGuideArtifactGenerators(TraverseTypes.Moving, channelType, method));
    }
    if (isEnableMapperOfMovingGuideGeneration(channelType)) {
      generators.addAll(
          makeGuideArtifactGenerators(TraverseTypes.MappingOfMoving, channelType, method));
    }
    return generators;
  }

  static List<ArtifactGenerator> makeDomainArtifactGenerators(CustomType domainType, ArtifactGeneratorContext context) {
    List<ArtifactGenerator> generators = new ArrayList<>();
    for (MethodStatement method : domainType.declaredMethods()) {
      if (method.hasAnnotation(Channel.class)) {
        if (isAutoGenerationEnabled(domainType, ArtifactTypes.Channel, context.roundEnvironment())) {
          generators.add(new ChannelGenerator(domainType, method));
        }
      }
    }
    addBasicObjectHandleGenerators(domainType, generators, context.roundEnvironment());
    addDownwardObjectHandleGenerators(domainType, generators);
    addIncludedGenerators(domainType, generators, context);
    return generators;
  }

  private static void addIncludedGenerators(
      CustomType annotatedType, List<ArtifactGenerator> generators, ArtifactGeneratorContext context
  ) {
    List<ArtifactProcessor> processors = AnnotationFunctions.allAnnotationsOf(
        annotatedType, AnnotationProcessor.class
    ).stream()
        .map(AnnotationProcessorFunctions::getAnnotationProcessorClass)
        .distinct()
        .map(c -> (ArtifactProcessor) Objects.get(c))
        .toList();
    for (ArtifactProcessor processor : processors) {
      if (processor.isApplicable(annotatedType)) {
        ArtifactValidator validator = processor.validator();
        if (validator != null) {
          validator.validate(annotatedType);
        }
        generators.addAll(processor.makeGenerators(annotatedType, context));
      }
    }
  }

  private static Class<?> getAnnotationProcessorClass(AnnotationProcessor annotationProcessor) {
    if (annotationProcessor.value() != Void.class) {
      return annotationProcessor.value();
    }
    if (StringFunctions.isNotBlank(annotationProcessor.name())) {
      String className = annotationProcessor.name().trim();
      return Classes.get(className).orElseThrow(() -> UnexpectedExceptions.withMessage("Could not find class {0}", className));
    }
    throw UnexpectedExceptions.withMessage("Invalid usage of annotation {0}", AnnotationProcessor.class.getSimpleName());
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

  private static void addDownwardObjectHandleGenerators(CustomType domainType, List<ArtifactGenerator> generators) {
    List<CustomTypeReference> parents = domainType.parentTypes();
    if (parents.size() != 1) {
      return;
    }
    CustomTypeReference parentDomainType = parents.get(0);
    generators.add(new UnmovableDownwardObjectHandleGenerator(domainType, parentDomainType));
    generators.add(new MovableDownwardObjectHandleGenerator(domainType, parentDomainType));
  }

  static List<ArtifactGenerator> makeOntologyArtifactGenerators(
      CustomType ontologyType, ArtifactGeneratorContext context
  ) {
    List<ArtifactGenerator> generators = new ArrayList<>();
    for (MethodStatement method : ontologyType.declaredMethods()) {
      if (method.hasAnnotation(Channel.class)) {
        if (isAutoGenerationEnabled(ontologyType, ArtifactTypes.Channel, context.roundEnvironment())) {
          generators.add(new tech.intellispaces.jaquarius.annotationprocessor.ontology.ChannelGenerator(ontologyType, method));
        }
      }
    }
    return generators;
  }

  private static List<ArtifactGenerator> makeGuideArtifactGenerators(
      TraverseType traverseType, CustomType channelType, MethodStatement channelMethod
  ) {
    List<ArtifactGenerator> generators = new ArrayList<>();
    generators.add(
        makeGuideArtifactGenerators(
            ObjectReferenceForms.Object, traverseType, channelType, channelMethod));
    if (channelMethod.returnType().isPresent()) {
      TypeReference returnType = channelMethod.returnType().get();
      if (returnType.isCustomTypeReference()) {
        CustomTypeReference customTypeReference = returnType.asCustomTypeReferenceOrElseThrow();
        if (ClassFunctions.isPrimitiveWrapperClass(customTypeReference.targetType().canonicalName())) {
          generators.add(makeGuideArtifactGenerators(
              ObjectReferenceForms.Primitive, traverseType, channelType, channelMethod
          ));
        }
      }
    }
    return generators;
  }

  private static ArtifactGenerator makeGuideArtifactGenerators(
      ObjectReferenceForm targetForm,
      TraverseType traverseType,
      CustomType channelType,
      MethodStatement channelMethod
  ) {
    return new GuideGenerator(targetForm, traverseType, channelType, channelMethod);
  }

  static List<ArtifactGenerator> makeObjectHandleArtifactGenerators(CustomType objectHandleType) {
    if (ObjectHandleFunctions.isUnmovableObjectHandle(objectHandleType)) {
      return List.of(new UnmovableObjectHandleWrapperGenerator(objectHandleType));
    } else if (ObjectHandleFunctions.isMovableObjectHandle(objectHandleType)) {
      return List.of(new MovableObjectHandleWrapperGenerator(objectHandleType));
    } else {
      throw UnexpectedExceptions.withMessage("Could not define movable type of the object handle {0}",
          objectHandleType.canonicalName());
    }
  }

  static List<ArtifactGenerator> makeModuleArtifactGenerators(CustomType moduleType) {
    List<ArtifactGenerator> generators = new ArrayList<>();
    generators.add(new UnitWrapperGenerator(moduleType));
    Iterable<CustomType> includedUnits = ModuleFunctions.getIncludedUnits(moduleType);
    includedUnits.forEach(unit -> generators.add(new UnitWrapperGenerator(unit)));
    return generators;
  }

  static List<ArtifactGenerator> makePreprocessingArtifactGenerators(
      CustomType customType, ArtifactGeneratorContext context
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
        generators.addAll(makeDomainArtifactGenerators(preprocessingClass, context));
      } else  if (preprocessingClass.hasAnnotation(Module.class)) {
        generators.addAll(makeModuleArtifactGenerators(preprocessingClass));
      } else if (UnitFunctions.isUnitType(preprocessingClass)) {
        generators.add(new UnitWrapperGenerator(preprocessingClass));
      } else if (UnitFunctions.isGuideInterface(preprocessingClass)) {
        generators.add(new AutoGuideGenerator(preprocessingClass));
      } else if (preprocessingClass.hasAnnotation(ObjectHandle.class)) {
        if (preprocessingClass.asClass().isPresent()) {
          generators.addAll(makeObjectHandleArtifactGenerators(preprocessingClass));
        }
      } else if (preprocessingClass.hasAnnotation(Ontology.class)) {
        generators.addAll(makeOntologyArtifactGenerators(preprocessingClass, context));
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

  static boolean isEnableMapperGuideGeneration(CustomType channelType) {
    return channelType.hasParent(MappingChannel.class);
  }

  static boolean isEnableMoverGuideGeneration(CustomType channelType) {
    return channelType.hasParent(MovingChannel.class);
  }

  static boolean isEnableMapperOfMovingGuideGeneration(CustomType channelType) {
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
