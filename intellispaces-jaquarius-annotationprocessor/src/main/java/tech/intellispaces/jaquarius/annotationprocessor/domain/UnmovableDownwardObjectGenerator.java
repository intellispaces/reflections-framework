package tech.intellispaces.jaquarius.annotationprocessor.domain;

import tech.intellispaces.commons.annotation.processor.ArtifactGeneratorContext;
import tech.intellispaces.commons.collection.ArraysFunctions;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.reflection.customtype.CustomType;
import tech.intellispaces.commons.reflection.method.MethodStatement;
import tech.intellispaces.commons.reflection.reference.CustomTypeReference;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.commons.type.Types;
import tech.intellispaces.jaquarius.annotation.Channel;
import tech.intellispaces.jaquarius.annotation.ObjectHandle;
import tech.intellispaces.jaquarius.channel.Channel0;
import tech.intellispaces.jaquarius.channel.Channel1;
import tech.intellispaces.jaquarius.channel.ChannelFunction0;
import tech.intellispaces.jaquarius.channel.ChannelFunction1;
import tech.intellispaces.jaquarius.exception.TraverseException;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;
import tech.intellispaces.jaquarius.object.reference.MovabilityType;
import tech.intellispaces.jaquarius.object.reference.MovabilityTypes;
import tech.intellispaces.jaquarius.object.reference.MovableObjectHandle;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForm;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForms;
import tech.intellispaces.jaquarius.object.reference.ObjectHandles;
import tech.intellispaces.jaquarius.object.reference.UnmovableObjectHandle;
import tech.intellispaces.jaquarius.space.domain.DomainFunctions;
import tech.intellispaces.jaquarius.traverse.MappingTraverse;
import tech.intellispaces.jaquarius.traverse.TraverseTypes;

import java.util.Optional;
import java.util.stream.Stream;

public class UnmovableDownwardObjectGenerator extends ConversionObjectGenerator {

  public UnmovableDownwardObjectGenerator(
      CustomType annotatedType, CustomTypeReference superDomainType
  ) {
    super(annotatedType, superDomainType);
  }

  @Override
  public boolean isRelevant(ArtifactGeneratorContext context) {
    return true;
  }

  @Override
  protected ObjectReferenceForm getForm() {
    return ObjectReferenceForms.ObjectHandle;
  }

  @Override
  protected MovabilityType getMovabilityType() {
    return MovabilityTypes.Unmovable;
  }

  @Override
  public String generatedArtifactName() {
    return NameConventionFunctions.getUnmovableDownwardObjectTypename(
        sourceArtifact(), superDomainType.targetType());
  }

  @Override
  protected String templateName() {
    return "/unmovable_downward_object.template";
  }

  @Override
  protected boolean analyzeSourceArtifact(ArtifactGeneratorContext context) {
    addImports(
        ObjectHandle.class,
        UnmovableObjectHandle.class,
        Type.class,
        Types.class,
        Channel0.class,
        Channel1.class,
        ChannelFunction0.class,
        ChannelFunction1.class,
        MappingTraverse.class,
        MovableObjectHandle.class,
        ObjectHandles.class,
        TraverseException.class,
        UnexpectedExceptions.class
    );

    String unmovableObjectHandleName = addImportAndGetSimpleName(
        NameConventionFunctions.getUnmovablePlainObjectTypename(superDomainType.targetType().className(), false));

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
    addVariable("domainMethods", rawDomainMethods);
    addVariable("unmovableObjectHandleName", unmovableObjectHandleName);
    addVariable("domainClassSimpleName", domainClassSimpleName);
    addVariable("isAlias", isAlias);
    addVariable("primaryDomainSimpleName", primaryDomainSimpleName);
    addVariable("domainType", domainType);
    addVariable("objectHandleClassSimpleName", getObjectHandleSimpleName());
    addVariable("movableObjectHandleName", getMovableObjectHandleSimpleName());
    return true;
  }

  private void analyzeChildObjectHandleType() {
    childObjectHandleType = addImportAndGetSimpleName(
        NameConventionFunctions.getUnmovableObjectHandleTypename(sourceArtifact().className(), true)
    );
  }

  @Override
  protected Stream<MethodStatement> getObjectFormMethods(
      CustomType customType, ArtifactGeneratorContext context
  ) {
    return extractNotMovingMethods(customType, context);
  }

  private Stream<MethodStatement> extractNotMovingMethods(CustomType customType, ArtifactGeneratorContext context) {
    return buildActualType(superDomainType.targetType(), context).actualMethods().stream()
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
