package tech.intellispaces.jaquarius.annotationprocessor.domain;

import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.general.collection.ArraysFunctions;
import tech.intellispaces.general.type.Type;
import tech.intellispaces.general.type.Types;
import tech.intellispaces.jaquarius.annotation.Channel;
import tech.intellispaces.jaquarius.annotation.ObjectHandle;
import tech.intellispaces.jaquarius.channel.Channel0;
import tech.intellispaces.jaquarius.channel.Channel1;
import tech.intellispaces.jaquarius.channel.ChannelMethod0;
import tech.intellispaces.jaquarius.channel.ChannelMethod1;
import tech.intellispaces.jaquarius.channel.MappingChannel;
import tech.intellispaces.jaquarius.exception.TraverseException;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;
import tech.intellispaces.jaquarius.object.handle.ObjectHandleTypes;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleType;
import tech.intellispaces.jaquarius.space.domain.DomainFunctions;
import tech.intellispaces.jaquarius.traverse.TraverseTypes;
import tech.intellispaces.java.reflection.customtype.CustomType;
import tech.intellispaces.java.reflection.method.MethodStatement;
import tech.intellispaces.java.reflection.reference.CustomTypeReference;

import java.util.Optional;
import java.util.stream.Stream;

public class UnmovableDownwardObjectHandleGenerator extends ConversionObjectHandleGenerator {

  public UnmovableDownwardObjectHandleGenerator(
      CustomType annotatedType, CustomTypeReference parentDomainType
  ) {
    super(annotatedType, parentDomainType);
  }

  @Override
  public boolean isRelevant(ArtifactGeneratorContext context) {
    return true;
  }

  @Override
  protected ObjectHandleType getObjectHandleType() {
    return ObjectHandleTypes.Unmovable;
  }

  @Override
  public String generatedArtifactName() {
    return NameConventionFunctions.getUnmovableDownwardObjectHandleTypename(
        sourceArtifact(), parentDomainType.targetType());
  }

  @Override
  protected String templateName() {
    return "/unmovable_downward_object_handle.template";
  }

  @Override
  protected boolean analyzeSourceArtifact(ArtifactGeneratorContext context) {
    addImports(
        ObjectHandle.class,
        Type.class,
        Types.class,
        Channel0.class,
        Channel1.class,
        ChannelMethod0.class,
        ChannelMethod1.class,
        MappingChannel.class,
        TraverseException.class
    );

    String unmovableObjectHandleName = addToImportAndGetSimpleName(
        NameConventionFunctions.getUnmovableObjectHandleTypename(parentDomainType.targetType().className()));

    analyzeDomain();
    analyzeChildObjectHandleType();
    analyzeObjectHandleMethods(context);
    analyzeAlias();

    addVariable("classTypeParams", classTypeParams);
    addVariable("classTypeParamsBrief", classTypeParamsBrief);
    addVariable("domainTypeParamsBrief", domainTypeParamsBrief);
    addVariable("domainTypeArguments", domainTypeArguments);
    addVariable("parentDomainClassSimpleName", parentDomainClassSimpleName);
    addVariable("childObjectHandleType", childObjectHandleType);
    addVariable("childField", childFieldName);
    addVariable("methods", methods);
    addVariable("unmovableObjectHandleName", unmovableObjectHandleName);
    addVariable("domainClassSimpleName", domainClassSimpleName);
    addVariable("isAlias", isAlias);
    addVariable("primaryDomainSimpleName", primaryDomainSimpleName);
    addVariable("domainType", domainType);
    return true;
  }

  private void analyzeChildObjectHandleType() {
    childObjectHandleType = addToImportAndGetSimpleName(
        NameConventionFunctions.getUnmovableObjectHandleTypename(sourceArtifact().className())
    );
  }

  @Override
  protected Stream<MethodStatement> getObjectHandleMethods(
      CustomType customType, ArtifactGeneratorContext context
  ) {
    return buildActualType(parentDomainType.targetType(), context).actualMethods().stream()
        .filter(m -> excludeDeepConversionMethods(m, customType))
        .filter(this::isNotMovingMethod)
        .filter(DomainFunctions::isNotDomainClassGetter);
  }

  private boolean isNotMovingMethod(MethodStatement method) {
    Optional<Channel> channel = method.selectAnnotation(Channel.class);
    if (channel.isEmpty()) {
      return true;
    }
    return !ArraysFunctions.containsAny(
        channel.orElseThrow().allowedTraverse(), TraverseTypes.Moving, TraverseTypes.MappingOfMoving
    );
  }
}
