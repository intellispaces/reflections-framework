package tech.intellispaces.jaquarius.annotationprocessor.channel;

import tech.intellispaces.commons.annotation.processor.ArtifactGenerator;
import tech.intellispaces.commons.java.reflection.customtype.CustomType;
import tech.intellispaces.commons.java.reflection.method.MethodStatement;
import tech.intellispaces.commons.java.reflection.reference.CustomTypeReference;
import tech.intellispaces.commons.java.reflection.reference.TypeReference;
import tech.intellispaces.commons.type.ClassFunctions;
import tech.intellispaces.jaquarius.exception.ConfigurationExceptions;
import tech.intellispaces.jaquarius.object.reference.ObjectForm;
import tech.intellispaces.jaquarius.object.reference.ObjectForms;
import tech.intellispaces.jaquarius.traverse.MappingOfMovingTraverse;
import tech.intellispaces.jaquarius.traverse.MappingTraverse;
import tech.intellispaces.jaquarius.traverse.MovingTraverse;
import tech.intellispaces.jaquarius.traverse.TraverseType;
import tech.intellispaces.jaquarius.traverse.TraverseTypes;

import java.util.ArrayList;
import java.util.List;

public interface ChannelProcessorFunctions {

  static List<ArtifactGenerator> makeGenerators(CustomType channelType) {
    List<MethodStatement> methods = channelType.declaredMethods();
    if (methods.size() != 1) {
      throw ConfigurationExceptions.withMessage("Channel class should contain one method only. Check class {0}",
          channelType.canonicalName());
    }
    MethodStatement method = methods.get(0);

    List<ArtifactGenerator> generators = new ArrayList<>();
    if (isEnableMapperGuideGeneration(channelType)) {
      generators.addAll(makeGuideArtifactGenerators(TraverseTypes.Mapping, channelType, method));}
    if (isEnableMoverGuideGeneration(channelType)) {
      generators.addAll(makeGuideArtifactGenerators(TraverseTypes.Moving, channelType, method));}
    if (isEnableMapperOfMovingGuideGeneration(channelType)) {
      generators.addAll(makeGuideArtifactGenerators(TraverseTypes.MappingOfMoving, channelType, method));
    }
    return generators;
  }

  static boolean isEnableMapperGuideGeneration(CustomType channelType) {
    return channelType.hasParent(MappingTraverse.class);
  }

  static boolean isEnableMoverGuideGeneration(CustomType channelType) {
    return channelType.hasParent(MovingTraverse.class);
  }

  static boolean isEnableMapperOfMovingGuideGeneration(CustomType channelType) {
    return channelType.hasParent(MappingOfMovingTraverse.class);
  }

  static List<ArtifactGenerator> makeGuideArtifactGenerators(
      TraverseType traverseType,
      CustomType channelType,
      MethodStatement channelMethod
  ) {
    List<ArtifactGenerator> generators = new ArrayList<>();
    generators.add(makeGuideArtifactGenerators(ObjectForms.Simple, traverseType, channelType, channelMethod));
    if (channelMethod.returnType().isPresent()) {
      TypeReference returnType = channelMethod.returnType().get();
      if (returnType.isCustomTypeReference()) {
        CustomTypeReference customTypeReference = returnType.asCustomTypeReferenceOrElseThrow();
        if (ClassFunctions.isPrimitiveWrapperClass(customTypeReference.targetType().canonicalName())) {
          generators.add(makeGuideArtifactGenerators(
              ObjectForms.ObjectHandle, traverseType, channelType, channelMethod
          ));
        }
      }
    }
    return generators;
  }

  static ArtifactGenerator makeGuideArtifactGenerators(
      ObjectForm targetForm,
      TraverseType traverseType,
      CustomType channelType,
      MethodStatement channelMethod
  ) {
    return new GuideGenerator(targetForm, traverseType, channelType, channelMethod);
  }
}
